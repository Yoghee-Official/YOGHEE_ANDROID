package com.teamyoga.yoghee.feature.main

import com.teamyoga.yoghee.core.domain.model.MainHomeData

sealed interface MainUiState {
    object Loading : MainUiState
    data class Success(val data: MainHomeData) : MainUiState
    data class Error(val message: String) : MainUiState
}
