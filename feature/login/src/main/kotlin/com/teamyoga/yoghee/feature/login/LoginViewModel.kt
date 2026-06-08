package com.teamyoga.yoghee.feature.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamyoga.yoghee.core.domain.model.SsoLoginResult
import com.teamyoga.yoghee.core.domain.model.SsoType
import com.teamyoga.yoghee.core.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object Loading : LoginUiState
    data class Success(val result: SsoLoginResult) : LoginUiState
    data class Error(val message: String) : LoginUiState
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val kakaoLoginClient: KakaoLoginClient,
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onKakaoLogin(context: Context) {
        if (_uiState.value is LoginUiState.Loading) return
        _uiState.update { LoginUiState.Loading }
        viewModelScope.launch {
            runCatching {
                val token = kakaoLoginClient.login(context)
                authRepository.ssoLogin(token = token, ssoType = SsoType.KAKAO)
            }.onSuccess { result ->
                _uiState.update { LoginUiState.Success(result) }
            }.onFailure { throwable ->
                if (throwable is KakaoLoginCancelledException) {
                    _uiState.update { LoginUiState.Idle }
                } else {
                    _uiState.update { LoginUiState.Error(throwable.message ?: "Login failed") }
                }
            }
        }
    }

    fun consumeState() {
        _uiState.update { LoginUiState.Idle }
    }
}
