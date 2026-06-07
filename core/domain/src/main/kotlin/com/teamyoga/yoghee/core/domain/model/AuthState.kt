package com.teamyoga.yoghee.core.domain.model

/**
 * 앱 전체의 인증 상태. AuthRepository가 StateFlow<AuthState>로 노출하며,
 * 각 화면 ViewModel은 이 Flow를 collect 해서 로그인/로그아웃에 반응한다.
 *
 * - Unknown: 앱 시작 직후, DataStore에서 저장된 토큰을 아직 읽기 전 상태
 * - Authenticated: 유효한 토큰이 있어 로그인된 상태
 * - Unauthenticated: 토큰이 없거나 모두 만료된 상태
 */
sealed interface AuthState {
    data object Unknown : AuthState
    data class Authenticated(val token: AuthToken) : AuthState
    data object Unauthenticated : AuthState
}
