package com.teamyoga.yoghee.feature.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamyoga.yoghee.feature.main.components.BannerPager
import com.teamyoga.yoghee.feature.main.components.FloatingBottomNavigation
import com.teamyoga.yoghee.feature.main.components.MainHeader

@Composable
fun MainScreen(
    onGoSearch: () -> Unit,
    onGoCategory: () -> Unit,
    onGoProfile: () -> Unit,
    onGoDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val trainingType by viewModel.trainingType.collectAsStateWithLifecycle()
    
    MainScreen(
        onGoSearch = onGoSearch,
        onGoCategory = onGoCategory,
        onGoProfile = onGoProfile,
        onGoDetail = onGoDetail,
        uiState = state,
        trainingType = trainingType,
        onTrainingTypeChanged = viewModel::onTrainingTypeChanged,
        modifier = modifier
    )
}

@Composable
internal fun MainScreen(
    onGoSearch: () -> Unit,
    onGoCategory: () -> Unit,
    onGoProfile: () -> Unit,
    onGoDetail: (String) -> Unit,
    uiState: MainUiState,
    trainingType: TrainingType,
    onTrainingTypeChanged: (TrainingType) -> Unit,
    modifier: Modifier
) {
    val bgColor = MaterialTheme.colorScheme.background
    Scaffold(
        topBar = {
            MainHeader(
                trainingType = trainingType,
                onTrainingTypeChanged = onTrainingTypeChanged
            )
        },
        containerColor = bgColor,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                is MainUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is MainUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 100.dp)
                    ) {
                        item {
                            BannerPager(banners = uiState.data.banners, backgroundColor = bgColor)
                        }
                    }
                    //                    LazyColumn(
                    //                        modifier = Modifier.fillMaxSize(),
                    //                        contentPadding = PaddingValues(bottom = 100.dp)
                    //                    ) {
                    //                        items(
                    //                            items = state.items,
                    //                            key = { it.id },
                    //                            contentType = { it::class.java }
                    //                        ) { item ->
                    //                            when (item) {
                    //                                is MainItem.Banner -> BannerItem(banner = item)
                    //                                is MainItem.Product -> ProductItem(product = item, onClick = { onGoDetail(item.id) })
                    //                                is MainItem.Ad -> AdItem(ad = item)
                    //                            }
                    //                        }
                    //                    }
                }
                is MainUiState.Error -> {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

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
