package com.teamyoga.yoghee.feature.registerClass

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.component.YogheeHeader
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.GRAY
import com.teamyoga.yoghee.core.ui.theme.LAND_BROWN
import com.teamyoga.yoghee.core.ui.theme.SAND_BEIGE
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme
import com.teamyoga.yoghee.core.ui.util.noRippleClickable
import com.teamyoga.yoghee.feature.registerClass.components.AddScheduleButton
import com.teamyoga.yoghee.feature.registerClass.components.CalendarDate
import com.teamyoga.yoghee.feature.registerClass.components.ClassIntroductionSection
import com.teamyoga.yoghee.feature.registerClass.components.ClassPurposeSection
import com.teamyoga.yoghee.feature.registerClass.components.DateMultiSelectCalendar
import com.teamyoga.yoghee.feature.registerClass.components.ImageItem
import com.teamyoga.yoghee.feature.registerClass.components.ImagePickerGrid
import com.teamyoga.yoghee.feature.registerClass.components.ImageSource
import com.teamyoga.yoghee.feature.registerClass.components.ImageSourcePickerBottomSheet
import com.teamyoga.yoghee.feature.registerClass.components.LocationListItem
import com.teamyoga.yoghee.feature.registerClass.components.LocationRegisterButton
import com.teamyoga.yoghee.feature.registerClass.components.MultiSelectChipsSection
import com.teamyoga.yoghee.feature.registerClass.components.RegisterSectionTitle
import com.teamyoga.yoghee.feature.registerClass.components.ScheduleBottomSheet
import com.teamyoga.yoghee.feature.registerClass.components.ClassSchedule
import com.teamyoga.yoghee.feature.registerClass.components.ScheduleItemCard
import kotlinx.coroutines.launch

private const val TOTAL_STEPS = 7

// 전문 수련 유형: (파트너/임산부/펫/키즈 요가는 이용 대상으로 이동)
private val CLASS_TYPE_OPTIONS = listOf(
    "ashtanga" to "아쉬탕가",
    "iyengar" to "아헹가",
    "hatha" to "하타",
    "vinyasa" to "빈야사",
    "yin_yoga" to "인요가",
    "therapy" to "테라피",
    "meditation" to "명상",
    "sivananda" to "쉬바난다",
    "bepros_flow" to "비프로스플로우",
    "inside_flow" to "인사이드플로우",
    "flying_yoga" to "플라잉 요가",
    "props_yoga" to "소도구 요가",
    "others" to "기타",
)

private val CLASS_CATEGORY_OPTIONS = listOf(
    "unique_yoga" to "이색 요가",
    "traditional_yoga" to "전통 요가",
    "power" to "파워",
    "relax" to "릴렉스",
    "flow" to "플로우",
    "outdoor" to "야외",
    "indoor" to "실내",
    "advanced" to "숙련자",
    "beginner" to "초심자",
)

private val CLASS_USER_OPTIONS = listOf(
    "partner_yoga" to "파트너 요가",
    "prenatal_yoga" to "임산부 요가",
    "kids_yoga" to "키즈 요가",
    "women_only" to "여성 전용",
    "men_only" to "남성 전용",
    "unisex" to "남녀 공용",
    "pet_yoga" to "펫요가",
)

@Composable
fun OneDayClassRegisterScreen(
    typeIndex: Int,
    onBack: () -> Unit,
    onGoRegisterCenter: () -> Unit,
    onGoEditCenter: (String) -> Unit,
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
        onClassPurposesChange = viewModel::onClassPurposesChange,
        onCategoryCodesChange = viewModel::onCategoryCodesChange,
        onScheduleApplied = viewModel::onScheduleApplied,
        onScheduleEdit = viewModel::onScheduleEdit,
        onScheduleDelete = viewModel::onScheduleDelete,
        onLoadCenters = viewModel::loadCenters,
        onGoRegisterCenter = onGoRegisterCenter,
        onGoEditCenter = onGoEditCenter,
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
    onGoEditCenter: (String) -> Unit,
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
                .navigationBarsPadding()
                .background(SAND_BEIGE),
        ) {
            Box(modifier = Modifier.weight(1f)) {
                when (currentStep) {
                    1 -> Step1Content(
                        name = state.name,
                        description = state.description,
                        classPurposes = state.classPurposes,
                        onNameChange = onNameChange,
                        onDescriptionChange = onDescriptionChange,
                        onClassPurposesChange = onClassPurposesChange,
                        onBack = goPrevious,
                    )
                    2 -> Step2Content(
                        categoryCodes = state.categoryCodes,
                        onCategoryCodesChange = onCategoryCodesChange,
                        onBack = goPrevious,
                    )
                    3 -> Step3Content(
                        schedules = state.schedules,
                        onScheduleApplied = onScheduleApplied,
                        onScheduleEdit = onScheduleEdit,
                        onScheduleDelete = onScheduleDelete,
                        onBack = goPrevious,
                    )
                    4 -> Step4Content(
                        centersState = state.centersState,
                        onLoadCenters = onLoadCenters,
                        onBack = goPrevious,
                        onRegisterLocationClick = onGoRegisterCenter,
                        onEditCenterClick = onGoEditCenter,
                    )
                    5 -> Step5Content(
                        images = state.images,
                        onImageAdded = onImageAdded,
                        onImagesAdded = onImagesAdded,
                        onImageRemoved = onImageRemoved,
                        onImagesReordered = onImagesReordered,
                        onShowMessage = onShowMessage,
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
    classPurposes: Set<String>,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onClassPurposesChange: (Set<String>) -> Unit,
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
            ClassPurposeSection(
                selected = classPurposes,
                onSelectedChange = onClassPurposesChange,
            )
        }
    }
}

@Composable
private fun Step2Content(
    categoryCodes: Set<String>,
    onCategoryCodesChange: (Set<String>) -> Unit,
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
                selected = categoryCodes,
                onSelectedChange = onCategoryCodesChange,
                itemModifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
            MultiSelectChipsSection(
                title = "수련 카테고리",
                subTitle = "수련 카테고리에 목록별로 노출돼요!",
                options = CLASS_CATEGORY_OPTIONS,
                selected = categoryCodes,
                onSelectedChange = onCategoryCodesChange,
                itemModifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
            MultiSelectChipsSection(
                title = "이용 대상",
                subTitle = "참여 가능한 대상과 운영 조건을 선택해주세요.",
                options = CLASS_USER_OPTIONS,
                selected = categoryCodes,
                onSelectedChange = onCategoryCodesChange,
                itemModifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

private sealed interface SheetMode {
    data object Add : SheetMode
    data class Edit(val index: Int, val schedule: ClassSchedule) : SheetMode
    data class Copy(val schedule: ClassSchedule) : SheetMode
}

@Composable
private fun Step3Content(
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

@Composable
private fun Step4Content(
    centersState: CentersState,
    onLoadCenters: () -> Unit,
    onBack: () -> Unit,
    onRegisterLocationClick: () -> Unit,
    onEditCenterClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) { onLoadCenters() }

    Column(modifier = modifier.fillMaxSize()) {
        YogheeHeader(
            title = stringResource(R.string.one_day_class_register_step4_title),
            onBack = onBack,
            subTitle = stringResource(R.string.inquire),
            onSubTitleClick = {},
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            RegisterSectionTitle(
                title = "장소 등록하기",
                modifier = Modifier.padding(top = 20.dp),
            )
            LocationRegisterButton(
                onClick = onRegisterLocationClick,
                modifier = Modifier.padding(top = 16.dp),
            )
            CentersSection(
                centersState = centersState,
                onAddClass = onRegisterLocationClick,
                onEditAddress = onEditCenterClick,
                modifier = Modifier.padding(top = 33.dp),
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun CentersSection(
    centersState: CentersState,
    onAddClass: () -> Unit,
    onEditAddress: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (centersState) {
        CentersState.Idle -> Unit
        CentersState.Loading -> {
            Column(modifier = modifier.fillMaxWidth()) {
                RegisterSectionTitle(title = "요가원 불러오기")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = LAND_BROWN)
                }
            }
        }
        is CentersState.Success -> {
            if (centersState.centers.isEmpty()) return
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                RegisterSectionTitle(title = "요가원 불러오기")
                centersState.centers.forEach { center ->
                    LocationListItem(
                        date = formatCreatedAt(center.createdAt),
                        name = center.name,
                        location = center.address,
                        onAddClass = onAddClass,
                        onEditAddress = { onEditAddress(center.centerId) },
                    )
                }
            }
        }
        is CentersState.Error -> Unit
    }
}

// ISO-8601(예: 2026-08-01T16:18:37.131Z) 앞부분에서 yyyy-MM-dd만 추출.
// 파싱 실패 시 원본 반환.
private fun formatCreatedAt(createdAt: String): String {
    val datePart = createdAt.substringBefore('T', missingDelimiterValue = "")
    return if (datePart.length == 10) datePart else createdAt
}

@Composable
private fun Step5Content(
    images: List<ImageItem>,
    onImageAdded: (Uri) -> Unit,
    onImagesAdded: (List<Uri>) -> Unit,
    onImageRemoved: (Int) -> Unit,
    onImagesReordered: (Int, Int) -> Unit,
    onShowMessage: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var sheetVisible by rememberSaveable { mutableStateOf(false) }
    // 카메라 촬영 결과 콜백이 URI를 돌려주지 않으므로 요청 시점의 URI를 임시 보관
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) { success ->
        val uri = pendingCameraUri
        pendingCameraUri = null
        if (success && uri != null) onImageAdded(uri)
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = MAX_IMAGE_COUNT),
    ) { uris ->
        if (uris.isEmpty()) return@rememberLauncherForActivityResult
        val remaining = MAX_IMAGE_COUNT - images.size
        val accepted = uris.take(remaining)
        if (accepted.isNotEmpty()) onImagesAdded(accepted)
        if (uris.size > accepted.size) {
            onShowMessage("이미지는 최대 ${MAX_IMAGE_COUNT}장까지 등록할 수 있어요.")
        }
    }

    val launchCamera: () -> Unit = {
        val uri = createImageCaptureUri(context)
        pendingCameraUri = uri
        cameraLauncher.launch(uri)
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted) {
            launchCamera()
        } else {
            val activity = context.findActivity()
            val canShowRationale = activity != null &&
                ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.CAMERA,
                )
            if (!canShowRationale) {
                // "다시 묻지 않기" 선택 등으로 재요청 불가 → 앱 설정 화면으로 바로 이동
                onShowMessage("설정에서 카메라 권한을 허용해주세요.")
                context.openAppSettings()
            } else {
                onShowMessage("카메라 권한이 필요합니다.")
            }
        }
    }

    val onCameraSelected: () -> Unit = {
        sheetVisible = false
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA,
        ) == PackageManager.PERMISSION_GRANTED
        if (granted) launchCamera()
        else cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    val onGallerySelected: () -> Unit = {
        sheetVisible = false
        galleryLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
        )
    }

    Column(modifier = modifier.fillMaxSize()) {
        YogheeHeader(
            title = stringResource(R.string.one_day_class_register_step5_title),
            onBack = onBack,
            subTitle = stringResource(R.string.inquire),
            onSubTitleClick = {},
        )

        RegisterSectionTitle(
            title = "수련원 이미지 등록",
            subTitle = "드래그로 이미지 순서를 변경할 수 있어요.",
            modifier = Modifier.padding(
                start = 16.dp,
                end = 24.dp,
                top = 20.dp,
                bottom = 7.dp
            )
        )

        ImagePickerGrid(
            images = images,
            onAddClick = { sheetVisible = true },
            onDelete = onImageRemoved,
            onReorder = onImagesReordered,
            canAddMore = images.size < MAX_IMAGE_COUNT,
        )
    }

    if (sheetVisible) {
        ImageSourcePickerBottomSheet(
            onDismiss = { sheetVisible = false },
            onSelect = { source ->
                when (source) {
                    ImageSource.CAMERA -> onCameraSelected()
                    ImageSource.GALLERY -> onGallerySelected()
                }
            },
        )
    }

}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

private fun Context.openAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", packageName, null)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
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
            onClassPurposesChange = {},
            onCategoryCodesChange = {},
            onScheduleApplied = {},
            onScheduleEdit = { _, _ -> },
            onScheduleDelete = {},
            onLoadCenters = {},
            onGoRegisterCenter = {},
            onGoEditCenter = {},
            onImageAdded = {},
            onImagesAdded = {},
            onImageRemoved = {},
            onImagesReordered = { _, _ -> },
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
                classPurposes = emptySet(),
                onNameChange = {},
                onDescriptionChange = {},
                onClassPurposesChange = {},
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
                categoryCodes = emptySet(),
                onCategoryCodesChange = {},
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
                schedules = emptyList(),
                onScheduleApplied = {},
                onScheduleEdit = { _, _ -> },
                onScheduleDelete = {},
                onBack = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Step5Content Empty")
@Composable
private fun Step5ContentEmptyPreview() {
    YogheeTheme {
        Box(modifier = Modifier.background(SAND_BEIGE)) {
            Step5Content(
                images = emptyList(),
                onImageAdded = {},
                onImagesAdded = {},
                onImageRemoved = {},
                onImagesReordered = { _, _ -> },
                onShowMessage = {},
                onBack = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Step5Content With Images")
@Composable
private fun Step5ContentWithImagesPreview() {
    YogheeTheme {
        Box(modifier = Modifier.background(SAND_BEIGE)) {
            Step5Content(
                images = List(5) { index ->
                    ImageItem(id = "preview-$index", uri = Uri.parse("preview://image/$index"))
                },
                onImageAdded = {},
                onImagesAdded = {},
                onImageRemoved = {},
                onImagesReordered = { _, _ -> },
                onShowMessage = {},
                onBack = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Step4Content")
@Composable
private fun Step4ContentPreview() {
    YogheeTheme {
        Box(modifier = Modifier.background(SAND_BEIGE)) {
            Step4Content(
                centersState = CentersState.Success(
                    centers = listOf(
                        com.teamyoga.yoghee.core.domain.model.Center(
                            centerId = "center-1234abcd",
                            name = "정환요가원",
                            address = "경기 남양주시 다산중앙로123번길 22-26 899호",
                            createdAt = "2026-08-01T16:18:37.131Z",
                        ),
                    ),
                ),
                onLoadCenters = {},
                onBack = {},
                onRegisterLocationClick = {},
                onEditCenterClick = {},
            )
        }
    }
}
