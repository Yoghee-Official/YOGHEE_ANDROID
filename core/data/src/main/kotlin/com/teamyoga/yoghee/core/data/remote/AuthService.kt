package com.teamyoga.yoghee.core.data.remote

import com.teamyoga.yoghee.core.data.remote.model.SsoCallbackResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AuthService {
    @GET("auth/sso/callback")
    suspend fun ssoCallback(
        @Query("token") token: String,
        @Query("sso") sso: String,
    ): SsoCallbackResponse
}
