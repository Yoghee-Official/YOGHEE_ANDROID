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
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    init {
        loadTab(_uiState.value.selectedTabId)
    }

    fun onTabSelected(tabId: String) {
        if (_uiState.value.selectedTabId == tabId) return
        _uiState.update { it.copy(selectedTabId = tabId) }

        // 캐시된 Success는 재호출하지 않는다. 실패/로딩 상태였다면 재시도.
        val cached = _uiState.value.tabStates[tabId]
        if (cached !is TabContentState.Success) loadTab(tabId)
    }

    private fun loadTab(tabId: String) {
        _uiState.update { state ->
            state.copy(tabStates = state.tabStates + (tabId to TabContentState.Loading))    // 현재 값에서 tabStates만 바꿈
        }
        viewModelScope.launch {
            val next = runCatching { categoryRepository.getClassesByCategory(tabId) }
                .fold(
                    onSuccess = { TabContentState.Success(it) },
                    onFailure = { TabContentState.Error(it.message ?: "Unknown error") },
                )
            _uiState.update { state ->
                state.copy(tabStates = state.tabStates + (tabId to next))
            }
        }
    }
}
