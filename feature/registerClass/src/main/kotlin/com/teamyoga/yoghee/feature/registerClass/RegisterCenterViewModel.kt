package com.teamyoga.yoghee.feature.registerClass

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamyoga.yoghee.core.domain.model.CreateCenterParams
import com.teamyoga.yoghee.core.domain.repository.ClassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterCenterViewModel @Inject constructor(
    private val classRepository: ClassRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterCenterUiState())
    val uiState: StateFlow<RegisterCenterUiState> = _uiState.asStateFlow()

    fun onFormChange(form: CenterForm) = _uiState.update { it.copy(form = form) }

    fun submit() {
        if (_uiState.value.submitState is SubmitState.Loading) return
        _uiState.update { it.copy(submitState = SubmitState.Loading) }

        viewModelScope.launch {
            val form = _uiState.value.form
            runCatching {
                classRepository.createCenter(
                    CreateCenterParams(
                        name = form.name,
                        description = form.description,
                        depth1 = form.depth1,
                        depth2 = form.depth2,
                        depth3 = form.depth3,
                        roadAddress = form.roadAddress,
                        jibunAddress = form.jibunAddress,
                        zonecode = form.zonecode,
                        addressDetail = form.addressDetail,
                        amenityCodes = form.amenityCodes.toList(),
                        categoryCodes = emptyList(),
                    )
                )
            }.onSuccess {
                _uiState.update { it.copy(submitState = SubmitState.Success) }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        submitState = SubmitState.Error(
                            throwable.message ?: "요가원 등록에 실패했습니다."
                        )
                    )
                }
            }
        }
    }

    fun onErrorConsumed() {
        if (_uiState.value.submitState is SubmitState.Error) {
            _uiState.update { it.copy(submitState = SubmitState.Idle) }
        }
    }
}

data class RegisterCenterUiState(
    val form: CenterForm = CenterForm(),
    val submitState: SubmitState = SubmitState.Idle,
)
