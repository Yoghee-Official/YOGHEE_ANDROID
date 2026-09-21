package com.teamyoga.yoghee.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamyoga.yoghee.core.domain.model.ClassDetail
import com.teamyoga.yoghee.core.domain.repository.ClassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Success(val classDetail: ClassDetail) : DetailUiState
    data class Error(val message: String) : DetailUiState
}

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val classRepository: ClassRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    // 마지막으로 로드한 classId. 같은 값으로 재호출되면 중복 API 호출을 막는다.
    private var loadedClassId: String? = null

    fun load(classId: String) {
        if (classId.isBlank()) {
            _uiState.value = DetailUiState.Error("잘못된 classId 입니다.")
            return
        }
        if (loadedClassId == classId && _uiState.value is DetailUiState.Success) return

        loadedClassId = classId
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            _uiState.value = runCatching { classRepository.getClassDetail(classId) }
                .fold(
                    onSuccess = { DetailUiState.Success(it) },
                    onFailure = { DetailUiState.Error(it.message ?: "수련 상세 정보를 불러오지 못했습니다.") },
                )
        }
    }
}
