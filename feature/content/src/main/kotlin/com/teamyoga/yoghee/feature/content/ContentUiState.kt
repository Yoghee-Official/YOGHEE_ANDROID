package com.teamyoga.yoghee.feature.content

import com.teamyoga.yoghee.core.domain.model.FeedContent

sealed interface ContentUiState {
    object Loading : ContentUiState
    data class Success(val feed: FeedContent) : ContentUiState
    data class Error(val message: String) : ContentUiState
}
