package com.teamyoga.yoghee.feature.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamyoga.yoghee.core.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CategoryUiState(
            tabs = LocationTabs,
            selectedTabId = LocationTabs.first().id,
        )
    )
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    init {
        val state = _uiState.value
        loadTab(state.selectedTabId, state.selectedSort)
    }

    fun onTabSelected(tabId: String) {
        if (_uiState.value.selectedTabId == tabId) return
        _uiState.update { it.copy(selectedTabId = tabId) }
        loadTab(tabId, _uiState.value.selectedSort)
    }

    fun onSortSelected(sort: CategorySort) {
        if (_uiState.value.selectedSort == sort) return
        _uiState.update { it.copy(selectedSort = sort) }
        loadTab(_uiState.value.selectedTabId, sort)
    }

    private fun loadTab(address: String, sort: CategorySort) {
        _uiState.update { it.copy(tabState = TabContentState.Loading) }
        viewModelScope.launch {
            val next = runCatching { categoryRepository.getClassesByAddress(address, sort.id) }
                .fold(
                    onSuccess = { TabContentState.Success(it) },
                    onFailure = { TabContentState.Error(it.message ?: "Unknown error") },
                )
            _uiState.update { it.copy(tabState = next) }
        }
    }
}
