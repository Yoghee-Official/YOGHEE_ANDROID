package com.teamyoga.yoghee.core.data.remote.auth

import com.teamyoga.yoghee.core.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 모든 요청에 `Authorization: Bearer {accessToken}` 인증 헤더를 자동 부착
 *
 * 이 Interceptor는 일반 API용 OkHttpClient(@AppHttp)에만 부착된다.
 * AuthService는 인증 헤더가 필요 없는 별도 OkHttpClient(@AuthHttp) 사용
 */
@Singleton
class AuthInterceptor @Inject constructor(
    private val authRepository: AuthRepository,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // 저장된 토큰이 있으면 헤더 부착, 없으면 그대로 (서버가 401 반환할 것)
        val accessToken = authRepository.currentAccessToken()
        val request = if (accessToken != null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(request)
    }
}
