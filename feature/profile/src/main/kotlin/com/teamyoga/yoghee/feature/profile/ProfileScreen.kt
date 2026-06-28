package com.teamyoga.yoghee.feature.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.teamyoga.yoghee.core.domain.Member
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ProfileUiState(
    val member: Member? = null,
)

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        ProfileUiState(
            member = Member(memberId = "m_1", name = "홍길동"),
        ),
    )

    val uiState: StateFlow<ProfileUiState> = _uiState
}

@Composable
fun ProfileScreen(
    onGoMain: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val vm: ProfileViewModel = viewModel()
    val state by vm.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(16.dp),
    ) {
        Text(text = "마이페이지")

        val memberName = state.member?.name ?: "-"
        Text(
            text = "이름: $memberName",
            modifier = Modifier.padding(top = 12.dp),
        )

        Button(
            onClick = onGoMain,
            modifier = Modifier.padding(top = 16.dp),
        ) {
            Text(text = "홈으로")
        }
    }
}

