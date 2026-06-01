package com.teamyoga.yoghee.core.domain.repository

import com.teamyoga.yoghee.core.domain.model.SsoLoginResult
import com.teamyoga.yoghee.core.domain.model.SsoType

interface AuthRepository {
    suspend fun ssoLogin(token: String, ssoType: SsoType): SsoLoginResult
}
