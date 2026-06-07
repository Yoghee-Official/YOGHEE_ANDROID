package com.teamyoga.yoghee.core.data.remote

import com.teamyoga.yoghee.core.data.remote.model.RefreshTokenRequest
import com.teamyoga.yoghee.core.data.remote.model.SsoCallbackResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    /**
     * SSO 콜백. 소셜 SDK가 발급한 access token을 우리 서버에 전달하면,
     * 우리 서버는 SSO 공급자에게 사용자 정보를 확인 후 자체 access/refresh token을 발급한다.
     */
    @GET("auth/sso/callback")
    suspend fun ssoCallback(
        @Query("token") token: String,
        @Query("sso") sso: String,
    ): SsoCallbackResponse

    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest,
    ): SsoCallbackResponse
}
