package com.teamyoga.yoghee.feature.registerClass

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.theme.SAND_BEIGE
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme
import com.teamyoga.yoghee.feature.registerClass.components.ImageItem
import kotlinx.coroutines.launch

private const val TOTAL_STEPS = 6

@Composable
fun RegularClassRegisterScreen(
    onBack: () -> Unit,
    onGoRegisterCenter: () -> Unit,
    onGoComplete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegularClassRegisterViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.submitState) {
        if (state.submitState is SubmitState.Success) {
            onGoComplete()
        }
    }

    RegularClassRegisterContent(
        state = state,
        onBack = onBack,
        onNameChange = viewModel::onNameChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onClassPurposesChange = viewModel::onClassPurposesChange,
        onCategoryCodesChange = viewModel::onCategoryCodesChange,
        onLoadCenters = viewModel::loadCenters,
        onGoRegisterCenter = onGoRegisterCenter,
        onCenterSelected = viewModel::onCenterSelected,
        onImageAdded = viewModel::onImageAdded,
        onImagesAdded = viewModel::onImagesAdded,
        onImageRemoved = viewModel::onImageRemoved,
        onImagesReordered = viewModel::onImagesReordered,
        onSubmit = viewModel::submit,
        onErrorConsumed = viewModel::onErrorConsumed,
        modifier = modifier,
    )
}

@Composable
private fun RegularClassRegisterContent(
    state: RegularClassRegisterUiState,
    onBack: () -> Unit,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onClassPurposesChange: (Set<String>) -> Unit,
    onCategoryCodesChange: (Set<String>) -> Unit,
    onLoadCenters: () -> Unit,
    onGoRegisterCenter: () -> Unit,
    onCenterSelected: (String) -> Unit,
    onImageAdded: (Uri) -> Unit,
    onImagesAdded: (List<Uri>) -> Unit,
    onImageRemoved: (Int) -> Unit,
    onImagesReordered: (Int, Int) -> Unit,
    onSubmit: () -> Unit,
    onErrorConsumed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var currentStep by rememberSaveable { mutableIntStateOf(1) }
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarScope = rememberCoroutineScope()
    val onShowMessage: (String) -> Unit = { message ->
        snackbarScope.launch { snackbarHostState.showSnackbar(message) }
    }
    val isLoading = state.submitState is SubmitState.Loading

    LaunchedEffect(state.submitState) {
        val current = state.submitState
        if (current is SubmitState.Error) {
            snackbarHostState.showSnackbar(current.message)
            onErrorConsumed()
        }
    }

    val goPrevious: () -> Unit = {
        if (currentStep == 1) onBack() else currentStep--
    }
    val goNext: () -> Unit = {
        if (!isLoading) {
            if (currentStep < TOTAL_STEPS) currentStep++ else onSubmit()
        }
    }

    BackHandler(onBack = goPrevious)

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(
                    WindowInsets.navigationBars.union(WindowInsets.ime),
                )
                .background(SAND_BEIGE),
        ) {
            Box(modifier = Modifier.weight(1f)) {
                when (currentStep) {
                    1 -> ClassIntroStepContent(
                        title = stringResource(R.string.one_day_class_register_step1_title),
                        name = state.name,
                        description = state.description,
                        classPurposes = state.classPurposes,
                        onNameChange = onNameChange,
                        onDescriptionChange = onDescriptionChange,
                        onClassPurposesChange = onClassPurposesChange,
                        onBack = goPrevious,
                    )
                    2 -> ClassCenterStepContent(
                        title = stringResource(R.string.one_day_class_register_step4_title),
                        centersState = state.centersState,
                        selectedCenterId = state.selectedCenterId,
                        onCenterSelected = onCenterSelected,
                        onLoadCenters = onLoadCenters,
                        onBack = goPrevious,
                        onRegisterLocationClick = onGoRegisterCenter,
                    )
                    3 -> ClassImageStepContent(
                        title = stringResource(R.string.one_day_class_register_step5_title),
                        images = state.images,
                        onImageAdded = onImageAdded,
                        onImagesAdded = onImagesAdded,
                        onImageRemoved = onImageRemoved,
                        onImagesReordered = onImagesReordered,
                        onShowMessage = onShowMessage,
                        onBack = goPrevious,
                    )
                    4 -> ClassCategoryStepContent(
                        title = stringResource(R.string.one_day_class_register_step2_title),
                        categoryCodes = state.categoryCodes,
                        onCategoryCodesChange = onCategoryCodesChange,
                        onBack = goPrevious,
                    )
                    5 -> StepPlaceholderContent(step = 5, onBack = goPrevious)
                    6 -> StepPlaceholderContent(step = 6, onBack = goPrevious)
                }
            }
            RegisterBottomBar(
                currentStep = currentStep,
                totalSteps = TOTAL_STEPS,
                isLoading = isLoading,
                onPrevious = goPrevious,
                onNext = goNext,
            )
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding(),
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "RegularClassRegisterScreen")
@Composable
private fun RegularClassRegisterScreenPreview() {
    YogheeTheme {
        RegularClassRegisterContent(
            state = RegularClassRegisterUiState(),
            onBack = {},
            onNameChange = {},
            onDescriptionChange = {},
            onClassPurposesChange = {},
            onCategoryCodesChange = {},
            onLoadCenters = {},
            onGoRegisterCenter = {},
            onCenterSelected = {},
            onImageAdded = {},
            onImagesAdded = {},
            onImageRemoved = {},
            onImagesReordered = { _, _ -> },
            onSubmit = {},
            onErrorConsumed = {},
        )
    }
}

@Preview(showBackground = true, name = "RegularClassRegister Placeholder Step5")
@Composable
private fun RegularClassRegisterPlaceholderPreview() {
    YogheeTheme {
        Box(modifier = Modifier.background(SAND_BEIGE)) {
            StepPlaceholderContent(step = 5, onBack = {})
        }
    }
}
