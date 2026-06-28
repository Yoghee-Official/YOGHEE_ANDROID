package com.teamyoga.yoghee.feature.contentFeed

import com.teamyoga.yoghee.core.domain.model.FeedContent

sealed interface ContentFeedUiState {
    object Loading : ContentFeedUiState
    data class Success(val feed: FeedContent) : ContentFeedUiState
    data class Error(val message: String) : ContentFeedUiState
}
