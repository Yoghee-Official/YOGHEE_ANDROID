package com.teamyoga.yoghee.feature.category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CategoryScreen(
    onGoMain: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val vm: CategoryViewModel = viewModel()
    val state by vm.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(text = state.title)

        state.categories.forEach { category ->
            Text(
                text = "• ${category.title}",
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        Button(
            onClick = onGoMain,
            modifier = Modifier.padding(top = 16.dp),
        ) {
            Text(text = "홈으로")
        }
    }
}
