package com.teamyoga.yoghee.feature.registerClass

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamyoga.yoghee.core.domain.model.CenterDetail
import com.teamyoga.yoghee.core.domain.model.CreateCenterParams
import com.teamyoga.yoghee.core.domain.repository.ClassRepository
import com.teamyoga.yoghee.core.navigation.AppRoute
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
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val centerId: String? = savedStateHandle
        .get<String>(AppRoute.RegisterCenter.ARG_CENTER_ID)
        ?.takeIf { it.isNotBlank() }

    private val _uiState = MutableStateFlow(RegisterCenterUiState(isEditMode = centerId != null))
    val uiState: StateFlow<RegisterCenterUiState> = _uiState.asStateFlow()

    init {
        centerId?.let { loadCenterDetail(it) }
    }

    fun onFormChange(form: CenterForm) = _uiState.update { it.copy(form = form) }

    fun submit() {
        if (_uiState.value.submitState is SubmitState.Loading) return
        val form = _uiState.value.form
        if (!form.isValid()) {
            _uiState.update { it.copy(showRequiredErrors = true) }
            return
        }
        _uiState.update {
            it.copy(showRequiredErrors = false, submitState = SubmitState.Loading)
        }

        viewModelScope.launch {
            val params = CreateCenterParams(
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
            runCatching {
                if (centerId != null) {
                    classRepository.updateCenter(centerId, params)
                } else {
                    classRepository.createCenter(params)
                }
            }.onSuccess {
                _uiState.update { it.copy(submitState = SubmitState.Success) }
            }.onFailure { throwable ->
                val fallback = if (centerId != null) "요가원 수정에 실패했습니다." else "요가원 등록에 실패했습니다."
                _uiState.update {
                    it.copy(
                        submitState = SubmitState.Error(throwable.message ?: fallback)
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

    private fun loadCenterDetail(id: String) {
        _uiState.update { it.copy(loadState = LoadState.Loading) }
        viewModelScope.launch {
            runCatching { classRepository.getCenterDetail(id) }
                .onSuccess { detail ->
                    _uiState.update {
                        it.copy(
                            form = detail.toForm(),
                            loadState = LoadState.Success,
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            loadState = LoadState.Error(
                                throwable.message ?: "요가원 정보를 불러오지 못했습니다."
                            )
                        )
                    }
                }
        }
    }
}

data class RegisterCenterUiState(
    val form: CenterForm = CenterForm(),
    val submitState: SubmitState = SubmitState.Idle,
    val showRequiredErrors: Boolean = false,
    val isEditMode: Boolean = false,
    val loadState: LoadState = LoadState.Idle,
)

sealed interface LoadState {
    data object Idle : LoadState
    data object Loading : LoadState
    data object Success : LoadState
    data class Error(val message: String) : LoadState
}

private fun CenterForm.isValid(): Boolean =
    depth1.isNotBlank() &&
        depth2.isNotBlank() &&
        roadAddress.isNotBlank() &&
        zonecode.isNotBlank() &&
        name.isNotBlank()

private fun CenterDetail.toForm(): CenterForm = CenterForm(
    name = name,
    description = description,
    depth1 = depth1,
    depth2 = depth2,
    depth3 = depth3,
    roadAddress = roadAddress,
    jibunAddress = jibunAddress,
    zonecode = zonecode,
    addressDetail = addressDetail,
    amenityCodes = amenityCodes.toSet(),
)
