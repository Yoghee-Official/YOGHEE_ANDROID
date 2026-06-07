package com.teamyoga.yoghee.core.data.repository

import com.teamyoga.yoghee.core.data.local.TokenLocalDataSource
import com.teamyoga.yoghee.core.data.remote.AuthService
import com.teamyoga.yoghee.core.data.remote.model.RefreshTokenRequest
import com.teamyoga.yoghee.core.data.remote.model.SsoCallbackData
import com.teamyoga.yoghee.core.domain.model.AuthState
import com.teamyoga.yoghee.core.domain.model.AuthToken
import com.teamyoga.yoghee.core.domain.model.SsoLoginResult
import com.teamyoga.yoghee.core.domain.model.SsoType
import com.teamyoga.yoghee.core.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import retrofit2.HttpException

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val tokenStore: TokenLocalDataSource,
) : AuthRepository {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override val authState: StateFlow<AuthState> = tokenStore.tokenFlow
        .map { token -> token.toAuthState() }
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly, // 구독하기 전부터 미리 토큰을 읽어둠
            initialValue = AuthState.Unknown,
        )

    override fun currentAccessToken(): String? =
        (authState.value as? AuthState.Authenticated)?.token?.accessToken

    override suspend fun ssoLogin(token: String, ssoType: SsoType): SsoLoginResult {
        val response = authService.ssoCallback(token = token, sso = ssoType.code)
        val data = response.data

        // 응답이 정상이면 토큰을 저장
        data?.toAuthToken()?.let { tokenStore.save(it) }

        return SsoLoginResult(
            accessToken = data?.accessToken,
            refreshToken = data?.refreshToken,
            accessTokenExpiresIn = data?.accessTokenExpiresIn,
            refreshTokenExpiresIn = data?.refreshTokenExpiresIn,
        )
    }

    override suspend fun refreshAccessToken(): Boolean {
        // 로그인 상태가 아니면(토큰 없음 / refresh 만료) refresh 자체가 불가능
        val current = (authState.value as? AuthState.Authenticated)?.token ?: return false

        return runCatching {
            authService.refreshToken(
                RefreshTokenRequest(
                    accessToken = current.accessToken,
                    refreshToken = current.refreshToken,
                    accessTokenExpiresIn = current.accessTokenExpiresIn,
                    refreshTokenExpiresIn = current.refreshTokenExpiresIn,
                )
            )
        }.fold(
            onSuccess = { response ->
                val newToken = response.data?.toAuthToken()
                if (newToken != null) {
                    tokenStore.save(newToken)
                    true
                } else {
                    // 200 응답인데 data가 비어있다 = 서버 비정상. 토큰은 일단 보존하고 실패만 반환.
                    false
                }
            },
            onFailure = { error ->
                // 401(REFRESH_TOKEN_EXPIRED)일 때만 토큰을 무효화한다.
                // 네트워크 단절(IOException) 같은 일시적 오류로 토큰을 지우면 사용자가 멀쩡히 로그인 중이었는데 강제 로그아웃되는 문제 발생
                if (error is HttpException && error.code() == 401) {
                    tokenStore.clear()
                }
                false
            }
        )
    }

    override suspend fun logout() {
        tokenStore.clear()
    }

    // --- helpers ---

    /**
     * 저장된 토큰을 AuthState로 변환.
     * Refresh token까지 만료됐다면 자동로그인 불가이므로 Unauthenticated로 간주.
     */
    private fun AuthToken?.toAuthState(): AuthState = when {
        this == null || !isRefreshTokenValid() -> AuthState.Unauthenticated
        else -> AuthState.Authenticated(this)
    }

    /**
     * 서버 응답 데이터를 도메인 AuthToken으로 변환.
     * 4개 필드 중 하나라도 null이면 null 반환 (저장 불가).
     */
    private fun SsoCallbackData.toAuthToken(): AuthToken? {
        val access = accessToken ?: return null
        val refresh = refreshToken ?: return null
        val accessExp = accessTokenExpiresIn ?: return null
        val refreshExp = refreshTokenExpiresIn ?: return null

        return AuthToken.from(
            accessToken = access,
            refreshToken = refresh,
            accessTokenExpiresIn = accessExp,
            refreshTokenExpiresIn = refreshExp,
        )
    }
}
