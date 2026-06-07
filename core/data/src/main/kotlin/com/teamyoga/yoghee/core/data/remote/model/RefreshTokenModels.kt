package com.teamyoga.yoghee.core.data.remote.model

import kotlinx.serialization.Serializable

/**
 * /auth/refresh 요청 바디.
 * 서버 스펙: 로그인 시 발급된 토큰 모델을 그대로 다시 송신한다.
 */
@Serializable
data class RefreshTokenRequest(
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresIn: Long,
    val refreshTokenExpiresIn: Long,
)
