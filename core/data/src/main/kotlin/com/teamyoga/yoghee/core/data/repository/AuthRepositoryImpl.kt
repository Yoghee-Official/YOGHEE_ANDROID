package com.teamyoga.yoghee.core.data.repository

import com.teamyoga.yoghee.core.data.remote.AuthService
import com.teamyoga.yoghee.core.domain.model.SsoLoginResult
import com.teamyoga.yoghee.core.domain.model.SsoType
import com.teamyoga.yoghee.core.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
) : AuthRepository {
    override suspend fun ssoLogin(token: String, ssoType: SsoType): SsoLoginResult {
        val response = authService.ssoCallback(token = token, sso = ssoType.code)
        val data = response.data
        return SsoLoginResult(
            accessToken = data?.accessToken,
            refreshToken = data?.refreshToken,
            accessTokenExpiresIn = data?.accessTokenExpiresIn,
            refreshTokenExpiresIn = data?.refreshTokenExpiresIn
        )
    }
}
