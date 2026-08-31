package com.teamyoga.yoghee.feature.registerClass

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamyoga.yoghee.core.domain.repository.ClassRepository
import com.teamyoga.yoghee.feature.registerClass.components.ImageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class RegularClassRegisterViewModel @Inject constructor(
    private val classRepository: ClassRepository,
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

    // Step 5, 6이 placeholder라 실제 스케줄/가격 입력이 없어 API 호출은 스킵하고 완료 처리.
    // Step 5, 6 UI 확정 시 OneDayClassRegisterViewModel.submit()와 유사한 로직으로 교체 예정.
    fun submit() {
        if (_uiState.value.submitState is SubmitState.Loading) return
        _uiState.update { it.copy(submitState = SubmitState.Success) }
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
    val submitState: SubmitState = SubmitState.Idle,
    val centersState: CentersState = CentersState.Idle,
)
