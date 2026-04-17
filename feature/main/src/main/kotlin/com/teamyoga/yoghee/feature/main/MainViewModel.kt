package com.teamyoga.yoghee.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamyoga.yoghee.core.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _isSecondChecked = MutableStateFlow(false)
    val isSecondChecked: StateFlow<Boolean> = _isSecondChecked.asStateFlow()

    init {
        loadMainData(type = "O")
    }

    fun onToggleChanged(isSecondChecked: Boolean) {
        _isSecondChecked.value = isSecondChecked
        loadMainData(type = if (isSecondChecked) "R" else "O")
    }

    private fun loadMainData(type: String) {
        viewModelScope.launch {
            _uiState.value = MainUiState.Loading
            try {
                val data = mainRepository.getMainData(type = type)
                _uiState.value = MainUiState.Success(data)
            } catch (e: Exception) {
                _uiState.value = MainUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
