package com.teamyoga.yoghee.feature.registerClass

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.component.YogheeHeader
import com.teamyoga.yoghee.core.ui.theme.SAND_BEIGE
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme
import com.teamyoga.yoghee.feature.registerClass.components.AddScheduleButton
import com.teamyoga.yoghee.feature.registerClass.components.CalendarDate
import com.teamyoga.yoghee.feature.registerClass.components.DateMultiSelectCalendar
import com.teamyoga.yoghee.feature.registerClass.components.ImageItem
import com.teamyoga.yoghee.feature.registerClass.components.RegisterSectionTitle
import com.teamyoga.yoghee.feature.registerClass.components.ScheduleBottomSheet
import com.teamyoga.yoghee.feature.registerClass.components.ClassSchedule
import com.teamyoga.yoghee.feature.registerClass.components.ScheduleItemCard
import kotlinx.coroutines.launch

private const val TOTAL_STEPS = 6

@Composable
fun OneDayClassRegisterScreen(
    onBack: () -> Unit,
    onGoRegisterCenter: () -> Unit,
    onGoComplete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OneDayClassRegisterViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.submitState) {
        if (state.submitState is SubmitState.Success) {
            onGoComplete()
        }
    }

    OneDayClassRegisterContent(
        state = state,
        onBack = onBack,
        onNameChange = viewModel::onNameChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onClassPurposesChange = viewModel::onClassPurposesChange,
        onCategoryCodesChange = viewModel::onCategoryCodesChange,
        onScheduleApplied = viewModel::onScheduleApplied,
        onScheduleEdit = viewModel::onScheduleEdit,
        onScheduleDelete = viewModel::onScheduleDelete,
        onLoadCenters = viewModel::loadCenters,
        onGoRegisterCenter = onGoRegisterCenter,
        onCenterSelected = viewModel::onCenterSelected,
        onImageAdded = viewModel::onImageAdded,
        onImagesAdded = viewModel::onImagesAdded,
        onImageRemoved = viewModel::onImageRemoved,
        onImagesReordered = viewModel::onImagesReordered,
        onPriceChange = viewModel::onPriceChange,
        onDiscountEnabledChange = viewModel::onDiscountEnabledChange,
        onDiscountRateChange = viewModel::onDiscountRateChange,
        onDiscountDateChange = viewModel::onDiscountDateChange,
        onRefundRateChange = viewModel::onRefundRateChange,
        onNoticeChange = viewModel::onNoticeChange,
        onSubmit = viewModel::submit,
        onErrorConsumed = viewModel::onErrorConsumed,
        modifier = modifier,
    )
}

@Composable
private fun OneDayClassRegisterContent(
    state: OneDayClassRegisterUiState,
    onBack: () -> Unit,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onClassPurposesChange: (Set<String>) -> Unit,
    onCategoryCodesChange: (Set<String>) -> Unit,
    onScheduleApplied: (ClassSchedule) -> Unit,
    onScheduleEdit: (Int, ClassSchedule) -> Unit,
    onScheduleDelete: (Int) -> Unit,
    onLoadCenters: () -> Unit,
    onGoRegisterCenter: () -> Unit,
    onCenterSelected: (String) -> Unit,
    onImageAdded: (Uri) -> Unit,
    onImagesAdded: (List<Uri>) -> Unit,
    onImageRemoved: (Int) -> Unit,
    onImagesReordered: (Int, Int) -> Unit,
    onPriceChange: (String) -> Unit,
    onDiscountEnabledChange: (Boolean) -> Unit,
    onDiscountRateChange: (String) -> Unit,
    onDiscountDateChange: (Long?, Long?) -> Unit,
    onRefundRateChange: (hoursBeforeClass: Int, value: String) -> Unit,
    onNoticeChange: (String) -> Unit,
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
                    2 -> ClassCategoryStepContent(
                        title = stringResource(R.string.one_day_class_register_step2_title),
                        categoryCodes = state.categoryCodes,
                        onCategoryCodesChange = onCategoryCodesChange,
                        onBack = goPrevious,
                    )
                    3 -> OneDayScheduleStepContent(
                        schedules = state.schedules,
                        onScheduleApplied = onScheduleApplied,
                        onScheduleEdit = onScheduleEdit,
                        onScheduleDelete = onScheduleDelete,
                        onBack = goPrevious,
                    )
                    4 -> ClassCenterStepContent(
                        title = stringResource(R.string.one_day_class_register_step4_title),
                        centersState = state.centersState,
                        selectedCenterId = state.selectedCenterId,
                        onCenterSelected = onCenterSelected,
                        onLoadCenters = onLoadCenters,
                        onBack = goPrevious,
                        onRegisterLocationClick = onGoRegisterCenter,
                    )
                    5 -> ClassImageStepContent(
                        title = stringResource(R.string.one_day_class_register_step5_title),
                        images = state.images,
                        onImageAdded = onImageAdded,
                        onImagesAdded = onImagesAdded,
                        onImageRemoved = onImageRemoved,
                        onImagesReordered = onImagesReordered,
                        onShowMessage = onShowMessage,
                        onBack = goPrevious,
                    )
                    6 -> Step6Content(
                        price = state.price,
                        onPriceChange = onPriceChange,
                        discountEnabled = state.discountEnabled,
                        onDiscountEnabledChange = onDiscountEnabledChange,
                        discountRate = state.discountRate,
                        onDiscountRateChange = onDiscountRateChange,
                        discountStartMillis = state.discountStartMillis,
                        discountEndMillis = state.discountEndMillis,
                        onDiscountDateChange = onDiscountDateChange,
                        refundRate24 = state.refundRate24,
                        refundRate48 = state.refundRate48,
                        refundRate72 = state.refundRate72,
                        onRefundRateChange = onRefundRateChange,
                        noticeText = state.noticeText,
                        onNoticeChange = onNoticeChange,
                        onBack = goPrevious,
                    )
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

private sealed interface SheetMode {
    data object Add : SheetMode
    data class Edit(val index: Int, val schedule: ClassSchedule) : SheetMode
    data class Copy(val schedule: ClassSchedule) : SheetMode
}

@Composable
private fun OneDayScheduleStepContent(
    schedules: List<ClassSchedule>,
    onScheduleApplied: (ClassSchedule) -> Unit,
    onScheduleEdit: (Int, ClassSchedule) -> Unit,
    onScheduleDelete: (Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var sheetMode by remember { mutableStateOf<SheetMode?>(null) }
    // 캘린더에서 선택 중인 날짜: Apply 시점에 ClassSchedule에 병합되고 초기화됨
    var selectedDates by remember { mutableStateOf<Set<CalendarDate>>(emptySet()) }

    Column(modifier = modifier.fillMaxSize()) {
        YogheeHeader(
            title = stringResource(R.string.one_day_class_register_step3_title),
            onBack = onBack,
            subTitle = stringResource(R.string.inquire),
            onSubTitleClick = {},
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            RegisterSectionTitle(
                title = "추가하고 싶은 날짜를 선택해주세요.",
                subTitle = "수련 오픈하는 날짜만 선택 해주세요. 요기는 휴강을 설정하지 않아도 괜찮아요.",
                modifier = Modifier.padding(top = 20.dp, start = 24.dp),
            )
            DateMultiSelectCalendar(
                selected = selectedDates,
                modifier = Modifier.padding(top = 16.dp),
                onSelectedChange = { selectedDates = it },
            )
            AddScheduleButton(
                onClick = { sheetMode = SheetMode.Add },
                enabled = selectedDates.isNotEmpty(),
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 20.dp),
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                schedules.forEachIndexed { index, schedule ->
                    ScheduleItemCard(
                        schedule = schedule,
                        onEdit = {
                            selectedDates = schedule.dates
                            sheetMode = SheetMode.Edit(index = index, schedule = schedule)
                        },
                        onCopy = {
                            selectedDates = schedule.dates
                            sheetMode = SheetMode.Copy(schedule = schedule)
                        },
                        onDelete = { onScheduleDelete(index) },
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    sheetMode?.let { mode ->
        val initial = when (mode) {
            SheetMode.Add -> null
            is SheetMode.Edit -> mode.schedule
            is SheetMode.Copy -> mode.schedule
        }
        ScheduleBottomSheet(
            initial = initial,
            onDismiss = { sheetMode = null },
            onApply = { input ->
                val complete = input.copy(dates = selectedDates)
                when (mode) {
                    is SheetMode.Edit -> onScheduleEdit(mode.index, complete)
                    SheetMode.Add, is SheetMode.Copy -> onScheduleApplied(complete)
                }
                selectedDates = emptySet()
                sheetMode = null
            },
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "OneDayClassRegisterScreen")
@Composable
private fun OneDayClassRegisterScreenPreview() {
    YogheeTheme {
        OneDayClassRegisterContent(
            state = OneDayClassRegisterUiState(),
            onBack = {},
            onNameChange = {},
            onDescriptionChange = {},
            onClassPurposesChange = {},
            onCategoryCodesChange = {},
            onScheduleApplied = {},
            onScheduleEdit = { _, _ -> },
            onScheduleDelete = {},
            onLoadCenters = {},
            onGoRegisterCenter = {},
            onCenterSelected = {},
            onImageAdded = {},
            onImagesAdded = {},
            onImageRemoved = {},
            onImagesReordered = { _, _ -> },
            onPriceChange = {},
            onDiscountEnabledChange = {},
            onDiscountRateChange = {},
            onDiscountDateChange = { _, _ -> },
            onRefundRateChange = { _, _ -> },
            onNoticeChange = {},
            onSubmit = {},
            onErrorConsumed = {},
        )
    }
}

@Preview(showBackground = true, name = "OneDayScheduleStepContent")
@Composable
private fun OneDayScheduleStepContentPreview() {
    YogheeTheme {
        Box(modifier = Modifier.background(SAND_BEIGE)) {
            OneDayScheduleStepContent(
                schedules = emptyList(),
                onScheduleApplied = {},
                onScheduleEdit = { _, _ -> },
                onScheduleDelete = {},
                onBack = {},
            )
        }
    }
}
