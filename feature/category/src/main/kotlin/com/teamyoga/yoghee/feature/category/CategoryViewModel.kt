package com.teamyoga.yoghee.feature.category

import androidx.lifecycle.ViewModel
import com.teamyoga.yoghee.core.domain.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CategoryViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        CategoryUiState(
            categories = listOf(
                Category(categoryId = "c_1", title = "카테고리 A"),
                Category(categoryId = "c_2", title = "카테고리 B"),
                Category(categoryId = "c_3", title = "카테고리 C"),
            ),
        ),
    )

    val uiState: StateFlow<CategoryUiState> = _uiState
}
