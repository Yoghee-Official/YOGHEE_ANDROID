package com.teamyoga.yoghee.feature.login

import android.content.Context
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * 사용자가 카카오 로그인을 의도적으로 취소한 경우 발생하는 예외
 */
class KakaoLoginCancelledException : Exception("User cancelled Kakao login")

@Singleton
class KakaoLoginClient @Inject constructor() {

    suspend fun login(context: Context): String =
        suspendCancellableCoroutine { cont ->
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        Log.i(TAG, "카카오계정 로그인 취소됨")
                        cont.resumeWithException(KakaoLoginCancelledException())
                    } else {
                        Log.e(TAG, "카카오계정으로 로그인 실패", error)
                        cont.resumeWithException(error)
                    }
                } else if (token != null) {
                    Log.i(TAG, "카카오계정으로 로그인 성공")
                    cont.resume(token.accessToken)
                } else {
                    cont.resumeWithException(IllegalStateException("Kakao token is null"))
                }
            }

            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                    if (error != null) {
                        // 사용자가 의도적으로 로그인 취소한 경우
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            Log.i(TAG, "카카오톡 로그인 취소됨")
                            cont.resumeWithException(KakaoLoginCancelledException())
                            return@loginWithKakaoTalk
                        }

                        Log.e(TAG, "카카오톡으로 로그인 실패", error)

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                    } else if (token != null) {
                        Log.i(TAG, "카카오톡으로 로그인 성공")
                        cont.resume(token.accessToken)
                    } else {
                        cont.resumeWithException(IllegalStateException("Kakao token is null"))
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
            }
        }

    companion object {
        private const val TAG = "KakaoLoginClient"
    }
}