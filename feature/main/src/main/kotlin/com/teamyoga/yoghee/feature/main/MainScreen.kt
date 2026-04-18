package com.teamyoga.yoghee.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamyoga.yoghee.core.domain.model.MainSection
import com.teamyoga.yoghee.feature.main.components.BannerPager
import com.teamyoga.yoghee.feature.main.components.FloatingBottomNavigation
//import com.teamyoga.yoghee.feature.main.components.InterestedCenterSection
//import com.teamyoga.yoghee.feature.main.components.LayoutOrderSection
import com.teamyoga.yoghee.feature.main.components.MainHeader
//import com.teamyoga.yoghee.feature.main.components.NewReviewSection
//import com.teamyoga.yoghee.feature.main.components.TodayClassSection

@Composable
fun MainScreen(
    onGoSearch: () -> Unit,
    onGoCategory: () -> Unit,
    onGoProfile: () -> Unit,
    onGoDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val trainingType by viewModel.trainingType.collectAsStateWithLifecycle()

    MainScreen(
        onGoSearch = onGoSearch,
        onGoCategory = onGoCategory,
        onGoProfile = onGoProfile,
        onGoDetail = onGoDetail,
        uiState = uiState,
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
    modifier: Modifier = Modifier
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
                MainUiState.Loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
                is MainUiState.Success -> SuccessContent(
                    sections = uiState.sections,
                    bgColor = bgColor
                )
                is MainUiState.Error -> Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
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

@Composable
private fun SuccessContent(
    sections: List<MainSection>,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        items(
            items = sections,
            key = { it::class.simpleName.orEmpty() },
            contentType = { it::class }
        ) { section ->
            when (section) {
                is MainSection.Banners ->
                    BannerPager(banners = section.banners, backgroundColor = bgColor)
//                is MainSection.TodayClasses ->
//                    TodayClassSection(classes = section.classes)
//                is MainSection.InterestedCenters ->
//                    InterestedCenterSection(centers = section.centers)
//                is MainSection.NewReviews ->
//                    NewReviewSection(reviews = section.reviews)
//                is MainSection.LayoutOrders ->
//                    LayoutOrderSection(layoutOrders = section.layoutOrders)
                else -> {}
            }
        }
    }
}
