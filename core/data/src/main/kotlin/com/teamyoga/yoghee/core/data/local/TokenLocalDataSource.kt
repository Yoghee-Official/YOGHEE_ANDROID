package com.teamyoga.yoghee.core.data.local

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.teamyoga.yoghee.core.data.local.crypto.TokenCipher
import com.teamyoga.yoghee.core.domain.model.AuthToken
import java.io.IOException
import java.security.GeneralSecurityException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * 암호화된 토큰을 DataStore에 보관/조회하는 로컬 저장소
 *
 * - access/refresh 토큰 문자열은 [TokenCipher]로 AES-GCM 암호화한 뒤 DataStore에 저장한다.
 * - 만료 시각은 민감 정보가 아니므로 평문(Long)으로 저장한다.
 */
@Singleton
class TokenLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,  // 영구 저장소
    private val cipher: TokenCipher,    // AES-GCM 암호화 도구
) {

    val tokenFlow: Flow<AuthToken?> = dataStore.data.map { prefs -> prefs.readAndCleanup() }

    /**
     * 토큰 저장, 로그인 성공 / refresh 성공 시 호출됨
     */
    suspend fun save(token: AuthToken) {
        dataStore.edit { prefs ->
            prefs[KEY_ACCESS_TOKEN] = cipher.encrypt(token.accessToken)
            prefs[KEY_REFRESH_TOKEN] = cipher.encrypt(token.refreshToken)
            prefs[KEY_ACCESS_EXPIRES_IN] = token.accessTokenExpiresIn
            prefs[KEY_REFRESH_EXPIRES_IN] = token.refreshTokenExpiresIn
            prefs[KEY_REFRESH_EXPIRES_AT] = token.refreshTokenExpiresAt
        }
    }

    /**
     * 토큰 삭제. 로그아웃 / refresh 실패 시 호출.
     */
    suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    /**
     * 토큰 읽기 + 손상 시 자동 정리.
     * 저장 흔적(KEY_ACCESS_TOKEN)이 있는데 토큰을 만들 수 없으면 손상으로 판단하고 비운다.
     * save()가 트랜잭션이라 정상 경로에선 일부만 남는 부분 저장 상태는 발생하지 않는다.
     */
    private suspend fun Preferences.readAndCleanup(): AuthToken? {
        val token = tryBuildToken()
        if (token == null && this[KEY_ACCESS_TOKEN] != null) {
            Log.w(TAG, "Stored token is corrupt (decrypt failed or partial save); clearing.")
            try {
                clear()   // 실패하면 초기화
            } catch (e: IOException) {
                Log.e(TAG, "Failed to clear corrupt token data", e)
            }
        }
        return token
    }

    // Preferences → AuthToken 변환. 필드 누락 / 복호화 실패 / 기타 에러 시 null. side effect 없음.
    private fun Preferences.tryBuildToken(): AuthToken? {
        val encryptedAccess = this[KEY_ACCESS_TOKEN] ?: return null
        val encryptedRefresh = this[KEY_REFRESH_TOKEN] ?: return null
        val accessExpiresIn = this[KEY_ACCESS_EXPIRES_IN] ?: return null
        val refreshExpiresIn = this[KEY_REFRESH_EXPIRES_IN] ?: return null
        val refreshExpiresAt = this[KEY_REFRESH_EXPIRES_AT] ?: return null

        return try {
            AuthToken(
                accessToken = cipher.decrypt(encryptedAccess),
                refreshToken = cipher.decrypt(encryptedRefresh),
                accessTokenExpiresIn = accessExpiresIn,
                refreshTokenExpiresIn = refreshExpiresIn,
                refreshTokenExpiresAt = refreshExpiresAt,
            )
        } catch (e: GeneralSecurityException) { // Tink 복호화 실패 (키셋 망가짐 / 변조 / 백업 복원 후 키 불일치)
            null
        } catch (e: IllegalArgumentException) { // Base64.decode 실패 (잘못된 인코딩)
            null
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error while decrypting token", e)
            null
        }
    }

    companion object {
        private const val TAG = "TokenLocalDataSource"

        private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val KEY_ACCESS_EXPIRES_IN = longPreferencesKey("access_expires_in")
        private val KEY_REFRESH_EXPIRES_IN = longPreferencesKey("refresh_expires_in")
        private val KEY_REFRESH_EXPIRES_AT = longPreferencesKey("refresh_expires_at")
    }
}
