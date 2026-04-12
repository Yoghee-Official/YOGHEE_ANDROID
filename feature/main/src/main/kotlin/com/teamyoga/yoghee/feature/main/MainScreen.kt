package com.teamyoga.yoghee.feature.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.teamyoga.yoghee.core.domain.MainItem
import com.teamyoga.yoghee.feature.main.components.AdItem
import com.teamyoga.yoghee.feature.main.components.BannerItem
import com.teamyoga.yoghee.feature.main.components.FloatingBottomNavigation
import com.teamyoga.yoghee.feature.main.components.MainHeader
import com.teamyoga.yoghee.feature.main.components.ProductItem

@Composable
fun MainScreen(
    onGoSearch: () -> Unit,
    onGoCategory: () -> Unit,
    onGoProfile: () -> Unit,
    onGoDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val vm: MainViewModel = viewModel()
    val state by vm.uiState.collectAsState()

    Scaffold(
        topBar = { MainHeader() },
        containerColor = MaterialTheme.colorScheme.background, // 테마에 등록한 #EFEDEB 적용
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 1. 메인 목록 (세로 스크롤)
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(
                    items = state.items,
                    key = { it.id },
                    contentType = { it::class.java }
                ) { item ->
                    when (item) {
                        is MainItem.Banner -> BannerItem(banner = item)
                        is MainItem.Product -> ProductItem(product = item, onClick = { onGoDetail(item.id) })
                        is MainItem.Ad -> AdItem(ad = item)
                    }
                }
            }

            // 2. 하단 플로팅 네비게이션 메뉴
            FloatingBottomNavigation(
                onGoSearch = onGoSearch,
                onGoCategory = onGoCategory,
                onGoProfile = onGoProfile,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
            )
        }
    }
}
