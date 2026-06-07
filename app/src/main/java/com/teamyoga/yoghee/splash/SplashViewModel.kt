package com.teamyoga.yoghee.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamyoga.yoghee.core.domain.model.AuthState
import com.teamyoga.yoghee.core.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * 앱 시작 시 자동로그인 수행 후 Main으로 이동시킨다.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

    init {
        viewModelScope.launch {
            // 1. 초기 인증 상태 확정 대기
            val initialState = authRepository.authState
                .filter { it !is AuthState.Unknown }
                .first() // 플로우가 값을 방출할 때까지 코루틴이 일시중단,첫 값이 들어오면 그 값을 반환하고 플로우 구독 해제

            // 2. 유효한 refresh token이 있으면 새 access token 발급 시도
            if (initialState is AuthState.Authenticated) {
                authRepository.refreshAccessToken()
            }

            // 3. Main으로 이동시키도록 신호.
            _isReady.value = true
        }
    }
}
