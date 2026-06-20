package com.teamyoga.yoghee.feature.category

import com.teamyoga.yoghee.core.domain.model.CategoryClass

data class CategoryUiState(
    val tabs: List<CategoryTab> = CategoryTabs,
    val selectedTabId: String = CategoryTabs.first().id,
    val tabStates: Map<String, TabContentState> = emptyMap(),
) {
    val currentTabState: TabContentState
        get() = tabStates[selectedTabId] ?: TabContentState.Loading
}

sealed interface TabContentState {
    data object Loading : TabContentState
    data class Success(val classes: List<CategoryClass>) : TabContentState
    data class Error(val message: String) : TabContentState
}
