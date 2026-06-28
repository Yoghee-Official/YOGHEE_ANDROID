package com.teamyoga.yoghee.feature.contentFeed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamyoga.yoghee.core.domain.repository.FeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContentFeedViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ContentFeedUiState>(ContentFeedUiState.Loading)
    val uiState: StateFlow<ContentFeedUiState> = _uiState.asStateFlow()

    init {
        loadFeed()
    }

    private fun loadFeed() {
        viewModelScope.launch {
            _uiState.value = ContentFeedUiState.Loading
            _uiState.value = runCatching { feedRepository.getFeed() }
                .fold(
                    onSuccess = { ContentFeedUiState.Success(it) },
                    onFailure = { ContentFeedUiState.Error(it.message ?: "Unknown error") },
                )
        }
    }
}
