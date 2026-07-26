package com.teamyoga.yoghee.feature.registerClass

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.component.YogheeHeader
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.GRAY
import com.teamyoga.yoghee.core.ui.theme.LAND_BROWN
import com.teamyoga.yoghee.core.ui.theme.SAND_BEIGE
import com.teamyoga.yoghee.core.ui.theme.WHITE
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme
import com.teamyoga.yoghee.core.ui.util.noRippleClickable
import com.teamyoga.yoghee.feature.registerClass.components.AddScheduleButton
import com.teamyoga.yoghee.feature.registerClass.components.CalendarDate
import com.teamyoga.yoghee.feature.registerClass.components.ClassIntroductionSection
import com.teamyoga.yoghee.feature.registerClass.components.ClassPurposeSection
import com.teamyoga.yoghee.feature.registerClass.components.DateMultiSelectCalendar
import com.teamyoga.yoghee.feature.registerClass.components.MultiSelectChipsSection
import com.teamyoga.yoghee.feature.registerClass.components.RegisterSectionTitle
import com.teamyoga.yoghee.feature.registerClass.components.ScheduleBottomSheet

private const val TOTAL_STEPS = 7

private val CLASS_TYPE_OPTIONS = listOf(
    "아쉬탕가", "아헹가", "하타", "빈야사", "인요가",
    "테라피", "명상", "쉬바난다", "비프로스플로우",
    "인사이드플로우", "플라잉요가", "소도구요가", "파트너요가",
    "임산부 요가", "펫요가", "키즈요가", "기타",
)

private val CLASS_CATEGORY_OPTIONS = listOf(
    "이색 요가", "전통 요가", "파워", "릴렉스", "플로우",
    "야외", "실내", "숙련자", "초심자",
)

private val CLASS_USER_OPTIONS = listOf(
    "파트너 요가", "임산부 요가", "키즈 요가", "여성 전용",
    "남성 전용", "남녀공용", "펫요가",
)

@Composable
fun OneDayClassRegisterScreen(
    typeIndex: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OneDayClassRegisterViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.submitState) {
        if (state.submitState is SubmitState.Success) {
            onBack()
        }
    }

    OneDayClassRegisterContent(
        state = state,
        onBack = onBack,
        onNameChange = viewModel::onNameChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onClassTypesChange = viewModel::onClassTypesChange,
        onClassCategoriesChange = viewModel::onClassCategoriesChange,
        onClassUsersChange = viewModel::onClassUsersChange,
        onDatesChange = viewModel::onDatesChange,
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
    onClassTypesChange: (Set<String>) -> Unit,
    onClassCategoriesChange: (Set<String>) -> Unit,
    onClassUsersChange: (Set<String>) -> Unit,
    onDatesChange: (Set<CalendarDate>) -> Unit,
    onSubmit: () -> Unit,
    onErrorConsumed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var currentStep by remember { mutableIntStateOf(1) }
    val snackbarHostState = remember { SnackbarHostState() }
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
                .navigationBarsPadding()
                .background(SAND_BEIGE),
        ) {
            Box(modifier = Modifier.weight(1f)) {
                when (currentStep) {
                    1 -> Step1Content(
                        name = state.name,
                        description = state.description,
                        onNameChange = onNameChange,
                        onDescriptionChange = onDescriptionChange,
                        onBack = goPrevious,
                    )
                    2 -> Step2Content(
                        classTypes = state.classTypes,
                        classCategories = state.classCategories,
                        classUsers = state.classUsers,
                        onClassTypesChange = onClassTypesChange,
                        onClassCategoriesChange = onClassCategoriesChange,
                        onClassUsersChange = onClassUsersChange,
                        onBack = goPrevious,
                    )
                    3 -> Step3Content(
                        dates = state.dates,
                        onDatesChange = onDatesChange,
                        onBack = goPrevious,
                    )
                    else -> StepPlaceholderContent(step = currentStep, onBack = goPrevious)
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

@Composable
private fun Step1Content(
    name: String,
    description: String,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        YogheeHeader(
            title = stringResource(R.string.one_day_class_register_step1_title),
            onBack = onBack,
            subTitle = stringResource(R.string.inquire),
            onSubTitleClick = {},
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            ClassIntroductionSection(
                title = name,
                content = description,
                onTitleChange = onNameChange,
                onContentChange = onDescriptionChange,
            )
            ClassPurposeSection()
        }
    }
}

@Composable
private fun Step2Content(
    classTypes: Set<String>,
    classCategories: Set<String>,
    classUsers: Set<String>,
    onClassTypesChange: (Set<String>) -> Unit,
    onClassCategoriesChange: (Set<String>) -> Unit,
    onClassUsersChange: (Set<String>) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        YogheeHeader(
            title = stringResource(R.string.one_day_class_register_step2_title),
            onBack = onBack,
            subTitle = stringResource(R.string.inquire),
            onSubTitleClick = {},
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            MultiSelectChipsSection(
                title = "전문 수련 유형",
                subTitle = "상세페이지에 노출되는 수련 유형이예요.",
                options = CLASS_TYPE_OPTIONS,
                selected = classTypes,
                onSelectedChange = onClassTypesChange,
            )
            MultiSelectChipsSection(
                title = "수련 카테고리",
                subTitle = "수련 카테고리에 목록별로 노출돼요!",
                options = CLASS_CATEGORY_OPTIONS,
                selected = classCategories,
                onSelectedChange = onClassCategoriesChange,
            )
            MultiSelectChipsSection(
                title = "이용 대상",
                subTitle = "참여 가능한 대상과 운영 조건을 선택해주세요.",
                options = CLASS_USER_OPTIONS,
                selected = classUsers,
                onSelectedChange = onClassUsersChange,
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun Step3Content(
    dates: Set<CalendarDate>,
    onDatesChange: (Set<CalendarDate>) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showScheduleSheet by remember { mutableStateOf(false) }

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
                selected = dates,
                modifier = Modifier.padding(top = 16.dp),
                onSelectedChange = onDatesChange,
            )
            AddScheduleButton(
                onClick = { showScheduleSheet = true },
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 20.dp),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(200.dp)
                    .background(WHITE),
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showScheduleSheet) {
        ScheduleBottomSheet(
            onDismiss = { showScheduleSheet = false },
            onApply = { /* 추후 개발 */ },
        )
    }
}

@Composable
private fun StepPlaceholderContent(
    step: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        YogheeHeader(
            title = "Step $step",
            onBack = onBack,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            YogheeText(
                text = "Step $step (준비 중)",
                color = BLACK,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
private fun RegisterBottomBar(
    currentStep: Int,
    totalSteps: Int,
    isLoading: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isLastStep = currentStep == totalSteps
    val nextButtonLabel = if (isLastStep) {
        "완료"
    } else {
        stringResource(R.string.continue_page)
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SAND_BEIGE)
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        ProgressBar(currentStep = currentStep, totalSteps = totalSteps)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            YogheeText(
                text = stringResource(R.string.previous_page),
                color = GRAY,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(116.dp).noRippleClickable(onPrevious),
            )

            Box(
                modifier = Modifier
                    .height(48.dp)
                    .weight(1f)
                    .alpha(if (isLoading) 0.5f else 1f)
                    .paint(
                        painter = painterResource(R.drawable.btn_continue_class_register),
                        contentScale = ContentScale.FillBounds,
                    )
                    .noRippleClickable {
                        if (!isLoading) onNext()
                    },
                contentAlignment = Alignment.Center,
            ) {
                YogheeText(
                    text = nextButtonLabel,
                    color = BLACK,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun ProgressBar(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(currentStep.toFloat() / totalSteps.toFloat())
                .height(2.dp)
                .background(LAND_BROWN),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(GRAY),
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
            onClassTypesChange = {},
            onClassCategoriesChange = {},
            onClassUsersChange = {},
            onDatesChange = {},
            onSubmit = {},
            onErrorConsumed = {},
        )
    }
}

@Preview(showBackground = true, name = "Step1Content")
@Composable
private fun Step1ContentPreview() {
    YogheeTheme {
        Box(modifier = Modifier.background(SAND_BEIGE)) {
            Step1Content(
                name = "",
                description = "",
                onNameChange = {},
                onDescriptionChange = {},
                onBack = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Step2Content")
@Composable
private fun Step2ContentPreview() {
    YogheeTheme {
        Box(modifier = Modifier.background(SAND_BEIGE)) {
            Step2Content(
                classTypes = emptySet(),
                classCategories = emptySet(),
                classUsers = emptySet(),
                onClassTypesChange = {},
                onClassCategoriesChange = {},
                onClassUsersChange = {},
                onBack = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Step3Content")
@Composable
private fun Step3ContentPreview() {
    YogheeTheme {
        Box(modifier = Modifier.background(SAND_BEIGE)) {
            Step3Content(
                dates = emptySet(),
                onDatesChange = {},
                onBack = {},
            )
        }
    }
}
