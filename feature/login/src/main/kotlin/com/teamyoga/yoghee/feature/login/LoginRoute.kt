package com.teamyoga.yoghee.feature.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamyoga.yoghee.core.domain.model.SsoLoginResult

@Composable
fun LoginRoute(
    onLoginSuccess: (SsoLoginResult) -> Unit,
    onNaverClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onAppleClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() } // 스낵바 상태 관리

    LaunchedEffect(state) {
        when (val current = state) {
            is LoginUiState.Success -> {
                onLoginSuccess(current.result)
                viewModel.consumeState()
            }
            is LoginUiState.Error -> {
                snackbarHostState.showSnackbar(current.message)
                viewModel.consumeState()
            }
            else -> Unit
        }
    }

    Box(modifier = modifier.fillMaxSize().systemBarsPadding()) {
        LoginScreen(
            onKakaoClick = { viewModel.onKakaoLogin(context) },
            onNaverClick = onNaverClick,
            onGoogleClick = onGoogleClick,
            onAppleClick = onAppleClick,
        )
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}
