package com.teamyoga.yoghee.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamyoga.yoghee.core.domain.model.AuthState
import com.teamyoga.yoghee.core.domain.repository.AuthRepository
import com.teamyoga.yoghee.core.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _trainingType = MutableStateFlow(TrainingType.DAILY)
    val trainingType: StateFlow<TrainingType> = _trainingType.asStateFlow()

    /**
     * 현재 로그인 상태. UI(예: 하단 네비게이션의 로그인/로그아웃 버튼)에서 사용.
     * - Splash에서 authState 확정 후 Main에 진입하므로, MainViewModel 시점엔 Unknown이 거의 없다.
     *   다만 stateIn의 initialValue는 안전하게 false로 둔다.
     * - 토큰 변경(refresh)은 [AuthState.Authenticated] → Authenticated 라 boolean이 안 바뀜.
     */
    val isLoggedIn: StateFlow<Boolean> = authRepository.authState
        .map { it is AuthState.Authenticated }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    init {
        observeAuthState()
    }

    fun onTrainingTypeChanged(type: TrainingType) {
        if (_trainingType.value == type) return
        _trainingType.value = type
        loadMainData(type)
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    /**
     * 로그인 상태 변화에 반응해서 화면 데이터 갱신
     */
    private fun observeAuthState() {
        viewModelScope.launch {
            authRepository.authState
                // 초기 Unknown 상태는 아직 토큰 읽기 전이므로 무시
                .filter { it !is AuthState.Unknown }
                // 로그인 여부만 추출. 토큰 instance가 바뀌어도 같은 로그인 상태면 무시.
                .map { it is AuthState.Authenticated }
                .distinctUntilChanged()
                .collect {
                    loadMainData(_trainingType.value)
                }
        }
    }

    private fun loadMainData(type: TrainingType) {
        viewModelScope.launch {
            _uiState.update { MainUiState.Loading }
            _uiState.value = runCatching { mainRepository.getMainData(type.apiCode) }
                .fold(
                    onSuccess = { MainUiState.Success(it) },
                    onFailure = { MainUiState.Error(it.message ?: "Unknown error") }
                )
        }
    }
}
