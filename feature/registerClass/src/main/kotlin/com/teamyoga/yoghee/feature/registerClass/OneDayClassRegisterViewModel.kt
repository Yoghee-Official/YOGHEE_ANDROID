package com.teamyoga.yoghee.feature.registerClass

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamyoga.yoghee.core.domain.model.ClassScheduleParam
import com.teamyoga.yoghee.core.domain.model.CreateOneDayClassParams
import com.teamyoga.yoghee.core.domain.repository.ClassRepository
import com.teamyoga.yoghee.feature.registerClass.components.CalendarDate
import com.teamyoga.yoghee.feature.registerClass.components.ClassSchedule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ONE_DAY_CLASS_TYPE = "O"

@HiltViewModel
class OneDayClassRegisterViewModel @Inject constructor(
    private val classRepository: ClassRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OneDayClassRegisterUiState())
    val uiState: StateFlow<OneDayClassRegisterUiState> = _uiState.asStateFlow()

    fun onNameChange(value: String) = _uiState.update { it.copy(name = value) }

    fun onDescriptionChange(value: String) = _uiState.update { it.copy(description = value) }

    fun onClassPurposesChange(value: Set<String>) =
        _uiState.update { it.copy(classPurposes = value) }

    fun onCategoryCodesChange(value: Set<String>) =
        _uiState.update { it.copy(categoryCodes = value) }

    fun onDatesChange(value: Set<CalendarDate>) = _uiState.update { it.copy(dates = value) }

    fun onScheduleApplied(input: ClassSchedule) {
        val current = _uiState.value
        val newSchedule = input.copy(dates = current.dates)
        _uiState.update {
            it.copy(
                schedules = it.schedules + newSchedule,
                dates = emptySet(),
            )
        }
    }

    fun onScheduleEdit(index: Int, input: ClassSchedule) {
        val current = _uiState.value
        if (index !in current.schedules.indices) return
        val updated = input.copy(dates = current.dates)
        _uiState.update {
            it.copy(
                schedules = it.schedules.toMutableList().apply { this[index] = updated },
                dates = emptySet(),
            )
        }
    }

    fun onScheduleDelete(index: Int) {
        val current = _uiState.value
        if (index !in current.schedules.indices) return
        _uiState.update {
            it.copy(
                schedules = it.schedules.toMutableList().apply { removeAt(index) },
            )
        }
    }

    fun submit() {
        if (_uiState.value.submitState is SubmitState.Loading) return
        _uiState.update { it.copy(submitState = SubmitState.Loading) }

        viewModelScope.launch {
            val state = _uiState.value
            runCatching {
                classRepository.createOneDayClass(
                    CreateOneDayClassParams(
                        type = ONE_DAY_CLASS_TYPE,
                        name = state.name,
                        description = state.description,
                        // TODO: 로그인한 강사 정보에서 centerId 채우기
                        centerId = "",
                        featureCodes = state.classPurposes.toList(),
                        categoryCodes = state.categoryCodes.toList(),
                        schedules = state.schedules.map { it.toParam() },
                    )
                )
            }.onSuccess {
                _uiState.update { it.copy(submitState = SubmitState.Success) }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        submitState = SubmitState.Error(
                            throwable.message ?: "클래스 등록에 실패했습니다."
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

    private fun ClassSchedule.toParam(): ClassScheduleParam = ClassScheduleParam(
        name = className,
        dates = dates
            .sortedWith(compareBy({ it.year }, { it.month }, { it.day }))
            .map { "%04d-%02d-%02d".format(it.year, it.month, it.day) },
        startTime = startTime,
        endTime = endTime,
        minCapacity = minCount,
        maxCapacity = maxCount,
    )
}

data class OneDayClassRegisterUiState(
    val name: String = "",
    val description: String = "",
    // 클래스 목적(featureCodes): API code 집합
    val classPurposes: Set<String> = emptySet(),
    val categoryCodes: Set<String> = emptySet(),
    val dates: Set<CalendarDate> = emptySet(),
    val schedules: List<ClassSchedule> = emptyList(),
    val submitState: SubmitState = SubmitState.Idle,
)

sealed interface SubmitState {
    data object Idle : SubmitState
    data object Loading : SubmitState
    data object Success : SubmitState
    data class Error(val message: String) : SubmitState
}
