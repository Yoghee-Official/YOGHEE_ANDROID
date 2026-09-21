package com.teamyoga.yoghee.feature.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DetailScreen(
    id: String,
    onGoMain: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // classId가 바뀌면 재로딩
    LaunchedEffect(id) {
        viewModel.load(id)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(16.dp),
    ) {
        when (val state = uiState) {
            DetailUiState.Loading -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
            )
            is DetailUiState.Success -> Column {
                Text(text = "상세 보기")
                Text(text = "classId = ${state.classDetail.classId}", modifier = Modifier.padding(top = 12.dp))
                Text(text = "name = ${state.classDetail.name}", modifier = Modifier.padding(top = 4.dp))
                Text(text = "rating = ${state.classDetail.rating}", modifier = Modifier.padding(top = 4.dp))

                Button(
                    onClick = onGoMain,
                    modifier = Modifier.padding(top = 16.dp),
                ) {
                    Text(text = "홈으로")
                }
            }
            is DetailUiState.Error -> Column(modifier = Modifier.align(Alignment.Center)) {
                Text(text = state.message)
                Button(
                    onClick = onGoMain,
                    modifier = Modifier.padding(top = 16.dp),
                ) {
                    Text(text = "홈으로")
                }
            }
        }
    }
}
