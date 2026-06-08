package com.teamyoga.yoghee.core.domain.model

data class SsoLoginResult(
    val accessToken: String?,
    val refreshToken: String?,
    val accessTokenExpiresIn: Long?,
    val refreshTokenExpiresIn: Long?
)

enum class SsoType(val code: String) {
    KAKAO("k"),
    NAVER("n"),
    GOOGLE("g"),
    APPLE("a"),
}
