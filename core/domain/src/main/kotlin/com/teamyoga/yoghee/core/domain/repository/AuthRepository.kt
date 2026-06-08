package com.teamyoga.yoghee.core.domain.repository

import com.teamyoga.yoghee.core.domain.model.AuthState
import com.teamyoga.yoghee.core.domain.model.SsoLoginResult
import com.teamyoga.yoghee.core.domain.model.SsoType
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val authState: StateFlow<AuthState>

    fun currentAccessToken(): String?

    suspend fun ssoLogin(token: String, ssoType: SsoType): SsoLoginResult

    suspend fun refreshAccessToken(): Boolean

    suspend fun logout()
}
