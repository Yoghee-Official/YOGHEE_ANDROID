package com.teamyoga.yoghee.feature.category

import com.teamyoga.yoghee.core.domain.Category

data class CategoryUiState(
    val title: String = "카테고리",
    val categories: List<Category> = emptyList(),
)
