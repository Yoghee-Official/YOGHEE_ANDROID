package com.teamyoga.yoghee.feature.category

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamyoga.yoghee.core.domain.model.CategoryClass
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme
import com.teamyoga.yoghee.feature.category.components.CategoryClassItem
import com.teamyoga.yoghee.feature.category.components.CategoryHeader
import com.teamyoga.yoghee.feature.category.components.CategoryTabChips
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.theme.GRAY

@Composable
fun CategoryScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CategoryViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CategoryScreen(
        onBack = onBack,
        state = state,
        onTabSelected = viewModel::onTabSelected,
        modifier = modifier,
    )
}

@Composable
internal fun CategoryScreen(
    onBack: () -> Unit,
    state: CategoryUiState,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = { CategoryHeader(onBack = onBack) },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            CategoryTabChips(
                tabs = state.tabs,
                selectedTabId = state.selectedTabId,
                onTabSelected = onTabSelected,
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                when (val tabState = state.currentTabState) {
                    TabContentState.Loading -> CircularProgressIndicator()

                    is TabContentState.Error -> ErrorScreen()

                    is TabContentState.Success -> {
                        if (tabState.classes.isEmpty()) {
                            ErrorScreen()
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(bottom = 24.dp),
                            ) {
                                items(
                                    items = tabState.classes,
                                    key = { "${state.selectedTabId}-${it.classId}" },
                                ) { item ->
                                    CategoryClassItem(item = item)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorScreen() {
    Text(
        text = stringResource(R.string.no_available_class),
        color = GRAY,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    )
}
private val previewClasses = listOf(
    CategoryClass(
        classId = "0fe4dfb5-ecac-4e53-8545-549e1c52c4cf",
        className = "봄맞이 힐링 요가 원데이",
        address = "경기 남양주시 다산동",
        images = listOf("a"),
        masterId = "m1",
        masterName = "김의영",
        rating = 4.8,
        review = 128,
        price = 35000,
        favoriteCount = 10,
        isFavorite = true,
    ),
    CategoryClass(
        classId = "543abf3f1-a5f8-42a9-b569",
        className = "플라잉 요가 체험 클래스",
        address = "경기 남양주시 다산동",
        images = emptyList(),
        masterId = "m2",
        masterName = "정환원장",
        rating = 0.0,
        review = 0,
        price = 20000,
        favoriteCount = 10,
        isFavorite = false,
    ),
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CategoryScreenSuccessPreview() {
    YogheeTheme {
        CategoryScreen(
            onBack = {},
            state = CategoryUiState(
                tabStates = mapOf(
                    CategoryTabs.first().id to TabContentState.Success(previewClasses),
                ),
            ),
            onTabSelected = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CategoryScreenLoadingPreview() {
    YogheeTheme {
        CategoryScreen(
            onBack = {},
            state = CategoryUiState(),
            onTabSelected = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CategoryScreenEmptyPreview() {
    YogheeTheme {
        CategoryScreen(
            onBack = {},
            state = CategoryUiState(
                tabStates = mapOf(
                    CategoryTabs.first().id to TabContentState.Success(emptyList()),
                ),
            ),
            onTabSelected = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CategoryScreenErrorPreview() {
    YogheeTheme {
        CategoryScreen(
            onBack = {},
            state = CategoryUiState(
                tabStates = mapOf(
                    CategoryTabs.first().id to TabContentState.Error("네트워크 오류가 발생했어요"),
                ),
            ),
            onTabSelected = {},
        )
    }
}
