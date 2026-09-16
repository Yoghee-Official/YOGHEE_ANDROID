package com.teamyoga.yoghee.feature.registerClass

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamyoga.yoghee.core.domain.model.Center
import com.teamyoga.yoghee.core.domain.model.ClassScheduleParam
import com.teamyoga.yoghee.core.domain.model.CreateClassPolicyParam
import com.teamyoga.yoghee.core.domain.model.CreateOneDayClassParams
import com.teamyoga.yoghee.core.domain.model.CreateRefundPolicyParam
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

private const val ONE_DAY_CLASS_TYPE = "O"
internal const val MAX_IMAGE_COUNT = 20

@HiltViewModel
class OneDayClassRegisterViewModel @Inject constructor(
    private val classRepository: ClassRepository,
    private val imageRepository: ImageRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OneDayClassRegisterUiState())
    val uiState: StateFlow<OneDayClassRegisterUiState> = _uiState.asStateFlow()

    fun onNameChange(value: String) = _uiState.update { it.copy(name = value) }

    fun onDescriptionChange(value: String) = _uiState.update { it.copy(description = value) }

    fun onClassPurposesChange(value: Set<String>) =
        _uiState.update { it.copy(classPurposes = value) }

    fun onCategoryCodesChange(value: Set<String>) =
        _uiState.update { it.copy(categoryCodes = value) }

    fun onCenterSelected(centerId: String) =
        _uiState.update { it.copy(selectedCenterId = centerId) }

    fun onScheduleApplied(input: ClassSchedule) {
        _uiState.update {
            it.copy(schedules = it.schedules + input)
        }
    }

    fun onScheduleEdit(index: Int, input: ClassSchedule) {
        val current = _uiState.value
        if (index !in current.schedules.indices) return
        _uiState.update {
            it.copy(
                schedules = it.schedules.toMutableList().apply { this[index] = input },
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

    fun onPriceChange(value: String) = _uiState.update { it.copy(price = value) }

    fun onDiscountEnabledChange(value: Boolean) =
        _uiState.update { it.copy(discountEnabled = value) }

    fun onDiscountRateChange(value: String) =
        _uiState.update { it.copy(discountRate = value) }

    fun onDiscountDateChange(startMillis: Long?, endMillis: Long?) =
        _uiState.update {
            it.copy(discountStartMillis = startMillis, discountEndMillis = endMillis)
        }

    fun onRefundRateChange(hoursBeforeClass: Int, value: String) =
        _uiState.update {
            when (hoursBeforeClass) {
                24 -> it.copy(refundRate24 = value)
                48 -> it.copy(refundRate48 = value)
                72 -> it.copy(refundRate72 = value)
                else -> it
            }
        }

    fun onNoticeChange(value: String) = _uiState.update { it.copy(noticeText = value) }

    fun submit() {
        if (_uiState.value.submitState is SubmitState.Loading) return

        val state = _uiState.value
        val price = state.price.toIntOrNull()
        if (price == null || price <= 0) {
            _uiState.update {
                it.copy(submitState = SubmitState.Error("1회수업 가격을 입력해주세요."))
            }
            return
        }

        _uiState.update { it.copy(submitState = SubmitState.Loading) }

        viewModelScope.launch {
            runCatching {
                val imageUrls = uploadImagesIfAny(imageRepository, state.images, context)
                classRepository.createOneDayClass(
                    CreateOneDayClassParams(
                        type = ONE_DAY_CLASS_TYPE,
                        name = state.name,
                        description = state.description,
                        centerId = state.selectedCenterId.orEmpty(),
                        featureCodes = state.classPurposes.toList(),
                        categoryCodes = state.categoryCodes.toList(),
                        schedules = state.schedules.map { it.toParam() },
                        images = imageUrls,
                        price = price,
                        policy = buildPolicy(state),
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

    // 사용자가 아무 값도 입력하지 않았으면 policy 자체를 null로 전송한다.
    private fun buildPolicy(state: OneDayClassRegisterUiState): CreateClassPolicyParam? {
        val discountRate = if (state.discountEnabled) {
            state.discountRate.toIntOrNull() ?: 0
        } else {
            0
        }
        val refundPolicies = listOf(
            24 to state.refundRate24,
            48 to state.refundRate48,
            72 to state.refundRate72,
        ).mapNotNull { (hours, raw) ->
            raw.toIntOrNull()?.let { rate ->
                CreateRefundPolicyParam(hoursBeforeClass = hours, refundRate = rate)
            }
        }
        val note = state.noticeText
        val noPolicyContent = discountRate == 0 && refundPolicies.isEmpty() && note.isBlank()
        if (noPolicyContent) return null
        return CreateClassPolicyParam(
            discountPrice = 0,
            discountRate = discountRate,
            reservationNote = note,
            refundPolicies = refundPolicies,
        )
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
    val schedules: List<ClassSchedule> = emptyList(),
    val images: List<ImageItem> = emptyList(),
    val selectedCenterId: String? = null,
    // Step 6: 가격/할인/환불/안내사항
    val price: String = "",
    val discountEnabled: Boolean = false,
    val discountRate: String = "",
    // 할인 적용 기간은 아직 서버 스펙에 필드가 없어 클라이언트 상태로만 유지.
    val discountStartMillis: Long? = null,
    val discountEndMillis: Long? = null,
    val refundRate24: String = "",
    val refundRate48: String = "",
    val refundRate72: String = "",
    val noticeText: String = "",
    val submitState: SubmitState = SubmitState.Idle,
    val centersState: CentersState = CentersState.Idle,
)

sealed interface SubmitState {
    data object Idle : SubmitState
    data object Loading : SubmitState
    data object Success : SubmitState
    data class Error(val message: String) : SubmitState
}

sealed interface CentersState {
    data object Idle : CentersState
    data object Loading : CentersState
    data class Success(val centers: List<Center>) : CentersState
    data class Error(val message: String) : CentersState
}
