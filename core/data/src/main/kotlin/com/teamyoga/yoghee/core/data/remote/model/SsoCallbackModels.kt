package com.teamyoga.yoghee.core.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class SsoCallbackResponse(
    val code: Int,
    val status: String,
    val data: SsoCallbackData? = null,
)

@Serializable
data class SsoCallbackData(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val accessTokenExpiresIn: Long? = null,
    val refreshTokenExpiresIn: Long? = null
)
