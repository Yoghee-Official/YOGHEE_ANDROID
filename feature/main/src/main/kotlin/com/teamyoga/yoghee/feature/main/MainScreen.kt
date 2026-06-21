package com.teamyoga.yoghee.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.teamyoga.yoghee.feature.main.components.InterestedClass
import com.teamyoga.yoghee.feature.main.components.MainHeader
import com.teamyoga.yoghee.feature.main.components.NewReviewSection
import com.teamyoga.yoghee.feature.main.components.RegisterClass
import com.teamyoga.yoghee.feature.main.components.Top10Class
import com.teamyoga.yoghee.feature.main.components.YogaCategorySection

@Composable
fun MainScreen(
    onGoSearch: () -> Unit,
    onGoCategory: () -> Unit,
    onGoLocation: () -> Unit,
    onGoProfile: () -> Unit,
    onGoDetail: (String) -> Unit,
    onGoLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val trainingType by viewModel.trainingType.collectAsStateWithLifecycle()
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()

    MainScreen(
        onGoSearch = onGoSearch,
        onGoCategory = onGoCategory,
        onGoLocation = onGoLocation,
        onGoProfile = onGoProfile,
        onGoDetail = onGoDetail,
        onGoLogin = onGoLogin,
        onLogout = viewModel::logout,
        isLoggedIn = isLoggedIn,
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
    onGoLocation: () -> Unit,
    onGoProfile: () -> Unit,
    onGoDetail: (String) -> Unit,
    onGoLogin: () -> Unit,
    onLogout: () -> Unit,
    isLoggedIn: Boolean,
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
                    bgColor = bgColor,
                    onGoCategory = onGoCategory,
                )
                is MainUiState.Error -> Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            FloatingBottomNavigation(
                isLoggedIn = isLoggedIn,
                onGoSearch = onGoSearch,
                onGoCategory = onGoCategory,
                onGoLocation = onGoLocation,
                onGoProfile = onGoProfile,
                onGoLogin = onGoLogin,
                onLogout = onLogout,
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
    onGoCategory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sectionModifier = Modifier.padding(top = 8.dp, bottom = 40.dp)
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        itemsIndexed(
            items = sections,
            key = { index, section -> "${section::class.simpleName}-$index" },
            contentType = { _, section -> section::class }
        ) { _, section ->
            when (section) {
                is MainSection.Banners ->
                    BannerPager(banners = section.banners)
                is MainSection.InterestedClassList ->
                    InterestedClass(
                        title = section.title,
                        classData = section.interestedClassList,
                        modifier = sectionModifier
                    )
                is MainSection.Top10Classes ->
                    Top10Class(
                        title = section.title,
                        classData = section.classes,
                        modifier = sectionModifier
                    )
                is MainSection.YogaCategory ->
                    YogaCategorySection(
                        title = section.title,
                        onCategoryClick = onGoCategory,
                        modifier = sectionModifier
                    )
                is MainSection.NewReviews ->
                    NewReviewSection(
                        reviews = section.reviews,
                        title = section.title,
                        modifier = sectionModifier
                    )
                else -> {}
            }
        }
        item {
            RegisterClass(modifier = sectionModifier)
        }
    }
}
