package com.teamyoga.yoghee.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamyoga.yoghee.core.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _trainingType = MutableStateFlow(TrainingType.DAILY)
    val trainingType: StateFlow<TrainingType> = _trainingType.asStateFlow()

    init {
        loadMainData(TrainingType.DAILY)
    }

    fun onTrainingTypeChanged(type: TrainingType) {
        if (_trainingType.value == type) return
        _trainingType.value = type
        loadMainData(type)
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
