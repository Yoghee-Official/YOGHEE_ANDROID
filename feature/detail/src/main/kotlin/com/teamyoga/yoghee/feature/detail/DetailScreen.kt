package com.teamyoga.yoghee.feature.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class DetailUiState(
    val id: String = "",
)

class DetailViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState

    fun setId(id: String) {
        _uiState.value = _uiState.value.copy(id = id)
    }
}

@Composable
fun DetailScreen(
    id: String,
    onGoMain: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val vm: DetailViewModel = viewModel()
    val state by vm.uiState.collectAsState()

    LaunchedEffect(id) {
        vm.setId(id)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(16.dp),
    ) {
        Text(text = "상세 보기")
        Text(text = "id = ${state.id}", modifier = Modifier.padding(top = 12.dp))

        Button(
            onClick = onGoMain,
            modifier = Modifier.padding(top = 16.dp),
        ) {
            Text(text = "홈으로")
        }
    }
}

