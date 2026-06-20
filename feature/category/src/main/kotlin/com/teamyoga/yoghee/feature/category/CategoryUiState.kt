package com.teamyoga.yoghee.feature.category

import com.teamyoga.yoghee.core.domain.model.CategoryClass

data class CategoryUiState(
    val tabs: List<CategoryTab> = CategoryTabs,
    val selectedTabId: String = CategoryTabs.first().id,
    val selectedSort: CategorySort = CategorySort.RECOMMEND,
    val tabState: TabContentState = TabContentState.Loading,
)

sealed interface TabContentState {
    data object Loading : TabContentState
    data class Success(val classes: List<CategoryClass>) : TabContentState
    data class Error(val message: String) : TabContentState
}

enum class CategorySort(val id: String, val label: String) {
    RECOMMEND("recommane", "추천순"),
    REVIEW("review", "리뷰많은순"),
    RECENT("recent", "최신순"),
    FAVORITE("favorite", "찜순"),
    EXPENSIVE("expensive", "가격높은순"),
    CHEAP("cheap", "가격낮은순"),
}
