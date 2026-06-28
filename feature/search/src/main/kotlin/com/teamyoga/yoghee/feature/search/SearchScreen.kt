package com.teamyoga.yoghee.feature.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class SearchUiState(
    val title: String = "검색",
    val query: String = "",
)

class SearchViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    fun setQuery(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
    }
}

@Composable
fun SearchScreen(
    onGoMain: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val vm: SearchViewModel = viewModel()
    val state by vm.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(16.dp),
    ) {
        Text(text = state.title)

        TextField(
            value = state.query,
            onValueChange = vm::setQuery,
            placeholder = { Text("검색어를 입력하세요") },
            singleLine = true,
            modifier = Modifier.padding(top = 12.dp),
        )

        Button(
            onClick = onGoMain,
            modifier = Modifier.padding(top = 12.dp),
        ) {
            Text(text = "홈으로")
        }
    }
}

