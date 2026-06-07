package com.teamyoga.yoghee.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamyoga.yoghee.core.domain.model.AuthState
import com.teamyoga.yoghee.core.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Splash 화면 ViewModel.
 *
 * 역할:
 *  - 앱 시작 직후 [AuthRepository.authState]가 [AuthState.Unknown]에서 벗어날 때까지 대기.
 *  - 토큰 상태가 확정되면 [isReady]가 true로 전환되어 Splash 화면이 Main으로 이동.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    authRepository: AuthRepository,
) : ViewModel() {

    val isReady: StateFlow<Boolean> = authRepository.authState
        .map { it !is AuthState.Unknown }   // 인증 상태가 Unknown이 아니면 true, Unknown이면 false
        .stateIn( // flow -> stateFlow 변환, 항상 최신값 보유
            scope = viewModelScope,
            started = SharingStarted.Eagerly,   // 구독자가 없어도 즉시 수집 시작
            initialValue = false,   // 초기값
        )
}
