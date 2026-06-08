package com.teamyoga.yoghee.core.data.remote.auth

import com.teamyoga.yoghee.core.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

/**
 * 서버가 401(Unauthorized)을 반환했을 때 refreshAccessToken() 호출하여 access 토큰을 갱신하고 원래 요청을 재시도
 *
 * 동작 흐름:
 *  1. 401 받음 → 이 메서드 호출됨
 *  2. AuthRepository.refreshAccessToken() 호출 → /auth/refresh 시도
 *  3. 성공 시: 새 access token으로 헤더를 갈아끼운 요청을 반환 → OkHttp가 자동 재시도
 *  4. 실패 시: null 반환 → 401이 그대로 호출부에 전파됨 (사용자 재로그인 유도)
 *
 * 동시성 보호:
 *  여러 요청이 동시에 만료된 토큰으로 401을 받으면, [refreshMutex]로 한 번에 하나만
 *  refresh 하도록 직렬화한다. 락 진입 후에는 "내가 보낸 토큰"과 "지금 저장된 토큰"이 다른지
 *  비교해서, 이미 다른 호출이 갱신했다면 refresh를 생략하고 새 토큰으로 바로 재시도한다.
 *
 * 무한 루프 방지: 이미 한 번 재시도한 요청은 [response.priorResponse]가 채워지므로 그 경우 포기.
 *
 * 이 Authenticator는 일반 API용 OkHttpClient(@AppHttp)에만 부착되므로
 * AuthService(/auth/refresh, /auth/sso/callback) 요청의 401은 이 곳을 거치지 않는다.
 */
@Singleton
class TokenAuthenticator @Inject constructor(
    private val authRepository: AuthRepository,
) : Authenticator {

    // 동시 refresh 요청을 직렬화하기 위한 lock
    private val refreshMutex = Mutex()

    /**
     * 401이 떨어지면 OkHttp가 자동으로 함수 호출
     * request 반환: Okhttp가 그걸로 재시도, null: 401 그대로 호출부에 전달
      */
    override fun authenticate(route: Route?, response: Response): Request? {
        // 이미 재시도한 요청에 또 401이 떨어진 경우 → 포기 (무한 루프 방지)
        if (response.priorResponse != null) return null

        // 이 요청이 보낼 때 사용했던 access token
        val staleAccessToken = response.request
            .header("Authorization")
            ?.removePrefix("Bearer ")

        // Authenticator는 동기 환경이라 runBlocking 사용
        return runBlocking {
            refreshMutex.withLock {
                // 락 진입 후 현재 저장된 토큰 확인
                // 다른 동시 401이 먼저 refresh를 끝냈다면, 토큰이 이미 새 값으로 바뀌어 있을 것
                val currentAccessToken = authRepository.currentAccessToken()
                if (currentAccessToken != null && currentAccessToken != staleAccessToken) {
                    // 이미 다른 호출이 갱신함 → 새 토큰으로 바로 재시도
                    return@withLock response.request.newBuilder()
                        .header("Authorization", "Bearer $currentAccessToken")
                        .build()
                }

                // 실제 refresh 시도
                val refreshed = authRepository.refreshAccessToken()
                if (!refreshed) return@withLock null

                // refresh 성공 → 새 토큰으로 헤더 갈아끼우고 재시도
                val newAccessToken = authRepository.currentAccessToken()
                    ?: return@withLock null

                response.request.newBuilder()
                    .header("Authorization", "Bearer $newAccessToken")
                    .build()
            }
        }
    }
}
