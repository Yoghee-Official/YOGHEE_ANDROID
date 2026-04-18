package com.teamyoga.yoghee.feature.main

import com.teamyoga.yoghee.core.domain.model.MainSection

sealed interface MainUiState {
    object Loading : MainUiState
    data class Success(val sections: List<MainSection>) : MainUiState
    data class Error(val message: String) : MainUiState
}
