package com.teamyoga.yoghee.core.domain.model

/**
 * 두 가지 시간 정보를 동시에 보관한다.
 * - [accessTokenExpiresIn] / [refreshTokenExpiresIn]: 서버가 발급 시점에 알려준 "남은 초" 원본
 * - [refreshTokenExpiresAt]: 발급 시점에 `now + refreshTokenExpiresIn*1000` 으로 계산한
 *   절대 만료 시각(epoch millis). 자동로그인 가능 여부 판단용.
 */
data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresIn: Long,
    val refreshTokenExpiresIn: Long,
    val refreshTokenExpiresAt: Long,
) {
    /**
     * Refresh Token이 아직 유효한지 검사. 만료되면 자동로그인 불가 → 재로그인 필요
     */
    fun isRefreshTokenValid(nowMs: Long = System.currentTimeMillis()): Boolean =
        nowMs < (refreshTokenExpiresAt - SAFETY_MARGIN_MS)

    companion object {
        // 디바이스/서버 시계 차이 및 네트워크 지연 고려 (30초)
        private const val SAFETY_MARGIN_MS = 30_000L

        fun from(
            accessToken: String,
            refreshToken: String,
            accessTokenExpiresIn: Long,
            refreshTokenExpiresIn: Long,
            issuedAtMs: Long = System.currentTimeMillis(),
        ): AuthToken = AuthToken(
            accessToken = accessToken,
            refreshToken = refreshToken,
            accessTokenExpiresIn = accessTokenExpiresIn,
            refreshTokenExpiresIn = refreshTokenExpiresIn,
            refreshTokenExpiresAt = issuedAtMs + refreshTokenExpiresIn * 1000L, // 만료 시각 계산
        )
    }
}
