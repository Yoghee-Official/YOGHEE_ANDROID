package com.teamyoga.yoghee.feature.main

import androidx.lifecycle.ViewModel
import com.teamyoga.yoghee.core.domain.MainItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        // 임시 가짜 데이터 로드
        loadMainItems()
    }

    // 임시 가짜 데이터 로드
    private fun loadMainItems() {
        _uiState.value = MainUiState(
            items = listOf(
                MainItem.Banner(id = "b1", imageUrl = "", linkUrl = ""),
                MainItem.Banner(id = "b2", imageUrl = "", linkUrl = ""),
                MainItem.Product(id = "p1", name = "고급 요가 매트", price = 45000, imageUrl = ""),
                MainItem.Product(id = "p2", name = "명상용 싱잉볼", price = 89000, imageUrl = ""),
                MainItem.Ad(id = "a1", adTitle = "신규 가입 이벤트", adContent = "지금 가입하고 5천원 쿠폰 받으세요", actionUrl = ""),
                MainItem.Product(id = "p3", name = "요가 블록 (2개 세트)", price = 15000, imageUrl = ""),
            )
        )
    }
}
