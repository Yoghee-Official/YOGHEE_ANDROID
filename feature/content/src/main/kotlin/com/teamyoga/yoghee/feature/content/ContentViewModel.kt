package com.teamyoga.yoghee.feature.content

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
class ContentViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ContentUiState>(ContentUiState.Loading)
    val uiState: StateFlow<ContentUiState> = _uiState.asStateFlow()

    init {
        loadFeed()
    }

    private fun loadFeed() {
        viewModelScope.launch {
            _uiState.value = ContentUiState.Loading
            _uiState.value = runCatching { feedRepository.getFeed() }
                .fold(
                    onSuccess = { ContentUiState.Success(it) },
                    onFailure = { ContentUiState.Error(it.message ?: "Unknown error") },
                )
        }
    }
}
