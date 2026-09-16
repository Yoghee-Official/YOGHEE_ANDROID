package com.teamyoga.yoghee.feature.registerClass

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamyoga.yoghee.core.domain.model.CreateRegularClassParams
import com.teamyoga.yoghee.core.domain.repository.ClassRepository
import com.teamyoga.yoghee.core.domain.repository.ImageRepository
import com.teamyoga.yoghee.feature.registerClass.components.ClassSchedule
import com.teamyoga.yoghee.feature.registerClass.components.ImageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

private const val REGULAR_CLASS_TYPE = "R"

@HiltViewModel
class RegularClassRegisterViewModel @Inject constructor(
    private val classRepository: ClassRepository,
    private val imageRepository: ImageRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegularClassRegisterUiState())
    val uiState: StateFlow<RegularClassRegisterUiState> = _uiState.asStateFlow()

    fun onNameChange(value: String) = _uiState.update { it.copy(name = value) }

    fun onDescriptionChange(value: String) = _uiState.update { it.copy(description = value) }

    fun onClassPurposesChange(value: Set<String>) =
        _uiState.update { it.copy(classPurposes = value) }

    fun onCategoryCodesChange(value: Set<String>) =
        _uiState.update { it.copy(categoryCodes = value) }

    fun onCenterSelected(centerId: String) =
        _uiState.update { it.copy(selectedCenterId = centerId) }

    fun onImageAdded(uri: Uri) {
        _uiState.update {
            if (it.images.size >= MAX_IMAGE_COUNT) it
            else it.copy(
                images = it.images + ImageItem(
                    id = UUID.randomUUID().toString(),
                    uri = uri,
                ),
            )
        }
    }

    fun onImagesAdded(uris: List<Uri>) {
        if (uris.isEmpty()) return
        _uiState.update {
            val remaining = MAX_IMAGE_COUNT - it.images.size
            if (remaining <= 0) return@update it
            val toAdd = uris.take(remaining).map { uri ->
                ImageItem(id = UUID.randomUUID().toString(), uri = uri)
            }
            it.copy(images = it.images + toAdd)
        }
    }

    fun onImageRemoved(index: Int) {
        val current = _uiState.value
        if (index !in current.images.indices) return
        _uiState.update {
            it.copy(images = it.images.toMutableList().apply { removeAt(index) })
        }
    }

    fun onHasHolidayChange(value: Boolean) {
        _uiState.update {
            // "휴무일이 없어요"로 바꾸면 이전에 선택된 요일도 초기화한다.
            if (value) it.copy(hasHoliday = true)
            else it.copy(hasHoliday = false, holidayDaysOfWeek = emptySet())
        }
    }

    fun onHolidayDayOfWeekToggle(dayCode: String) {
        _uiState.update {
            val current = it.holidayDaysOfWeek
            val next = if (dayCode in current) current - dayCode else current + dayCode
            it.copy(holidayDaysOfWeek = next)
        }
    }

    // pill 하나가 담당하는 코드 묶음을 통째로 추가/삭제.
    // 이미 모두 선택돼 있으면 해제, 그 외에는 모두 추가.
    fun onHolidayToggle(codes: Set<String>) {
        _uiState.update {
            val current = it.holidays
            val allSelected = codes.all { code -> code in current }
            val next = if (allSelected) current - codes else current + codes
            it.copy(holidays = next)
        }
    }

    // "전체 휴무" 체크박스 토글: 전체 선택 상태면 해제, 그 외에는 모두 선택.
    fun onAllHolidayToggle() {
        _uiState.update {
            val next = if (it.holidays == ALL_HOLIDAY_CODES) emptySet() else ALL_HOLIDAY_CODES
            it.copy(holidays = next)
        }
    }

    // Step 6: 특정 요일에 스케줄 등록. 같은 요일에 여러 스케줄이 등록될 수 있으므로 그대로 append.
    fun onScheduleAdded(dayCode: String, schedule: ClassSchedule) {
        _uiState.update {
            it.copy(schedules = it.schedules + ScheduleEntry(dayCode, schedule))
        }
    }

    fun onScheduleRemoved(entry: ScheduleEntry) {
        _uiState.update {
            it.copy(schedules = it.schedules - entry)
        }
    }

    // 리스트 내 원본 위치는 유지하고 스케줄만 새 값으로 교체.
    fun onScheduleUpdated(oldEntry: ScheduleEntry, newSchedule: ClassSchedule) {
        _uiState.update { state ->
            state.copy(
                schedules = state.schedules.map { entry ->
                    if (entry == oldEntry) ScheduleEntry(oldEntry.dayCode, newSchedule)
                    else entry
                },
            )
        }
    }

    fun onImagesReordered(from: Int, to: Int) {
        val current = _uiState.value
        if (from == to) return
        if (from !in current.images.indices || to !in current.images.indices) return
        _uiState.update {
            it.copy(
                images = it.images.toMutableList().apply { add(to, removeAt(from)) },
            )
        }
    }

    // 현재는 Step 1 필드(name, description, featureCodes)만 서버로 전송.
    // 나머지 Step 필드는 UI 확정 시 CreateRegularClassParams / Repository 매핑에 순차 추가.
    fun submit() {
        if (_uiState.value.submitState is SubmitState.Loading) return

        val state = _uiState.value
        _uiState.update { it.copy(submitState = SubmitState.Loading) }

        viewModelScope.launch {
            runCatching {
                val imageUrls = uploadImagesIfAny(imageRepository, state.images, context)
                classRepository.createRegularClass(
                    CreateRegularClassParams(
                        type = REGULAR_CLASS_TYPE,
                        name = state.name,
                        description = state.description,
                        centerId = state.selectedCenterId.orEmpty(),
                        featureCodes = state.classPurposes.toList(),
                        categoryCodes = state.categoryCodes.toList(),
                        images = imageUrls,
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

    fun loadCenters() {
        _uiState.update { it.copy(centersState = CentersState.Loading) }
        viewModelScope.launch {
            runCatching { classRepository.getCenters() }
                .onSuccess { centers ->
                    _uiState.update { it.copy(centersState = CentersState.Success(centers)) }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            centersState = CentersState.Error(
                                throwable.message ?: "요가원 목록을 불러오지 못했습니다."
                            )
                        )
                    }
                }
        }
    }
}

data class RegularClassRegisterUiState(
    val name: String = "",
    val description: String = "",
    val classPurposes: Set<String> = emptySet(),
    val categoryCodes: Set<String> = emptySet(),
    val images: List<ImageItem> = emptyList(),
    val selectedCenterId: String? = null,
    val hasHoliday: Boolean = true,
    val holidayDaysOfWeek: Set<String> = emptySet(),
    val holidays: Set<String> = ALL_HOLIDAY_CODES,
    val schedules: List<ScheduleEntry> = emptyList(),
    val submitState: SubmitState = SubmitState.Idle,
    val centersState: CentersState = CentersState.Idle,
)

// 정규수련 스케줄 그리드에서 등록된 스케줄. dayCode는 HOLIDAY_DAY_OF_WEEK_OPTIONS의 key(MON..SUN).
data class ScheduleEntry(
    val dayCode: String,
    val schedule: ClassSchedule,
)
