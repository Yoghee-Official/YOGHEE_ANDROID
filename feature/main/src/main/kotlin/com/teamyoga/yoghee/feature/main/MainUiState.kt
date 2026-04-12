package com.teamyoga.yoghee.feature.main

import com.teamyoga.yoghee.core.domain.MainItem

data class MainUiState(
    val items: List<MainItem> = emptyList(),
    val isLoading: Boolean = false
)
