package com.teamyoga.yoghee.feature.registerClass

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.teamyoga.yoghee.core.common.formatCreatedAt
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.component.YogheeHeader
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.GRAY
import com.teamyoga.yoghee.core.ui.theme.Green_D6F695
import com.teamyoga.yoghee.core.ui.theme.LAND_BROWN
import com.teamyoga.yoghee.core.ui.theme.LIGHT_GRAY
import com.teamyoga.yoghee.core.ui.theme.SAND_BEIGE
import com.teamyoga.yoghee.core.ui.theme.WHITE
import com.teamyoga.yoghee.core.ui.util.noRippleClickable
import com.teamyoga.yoghee.feature.registerClass.components.ClassIntroductionSection
import com.teamyoga.yoghee.feature.registerClass.components.ClassPurposeSection
import com.teamyoga.yoghee.feature.registerClass.components.ImageItem
import com.teamyoga.yoghee.feature.registerClass.components.ImagePickerGrid
import com.teamyoga.yoghee.feature.registerClass.components.ImageSource
import com.teamyoga.yoghee.feature.registerClass.components.ImageSourcePickerBottomSheet
import com.teamyoga.yoghee.feature.registerClass.components.LocationListItem
import com.teamyoga.yoghee.feature.registerClass.components.LocationRegisterButton
import com.teamyoga.yoghee.feature.registerClass.components.MultiSelectChipsSection
import com.teamyoga.yoghee.feature.registerClass.components.RegisterSectionTitle

// 수련 등록 화면에서 공통으로 사용하는 Step Content 및 하단 바 컴포넌트.
// OneDayClassRegisterScreen과 RegularClassRegisterScreen에서 재사용된다.

internal val CLASS_TYPE_OPTIONS = listOf(
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

internal val CLASS_CATEGORY_OPTIONS = listOf(
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

internal val CLASS_USER_OPTIONS = listOf(
    "partner_yoga" to "파트너 요가",
    "prenatal_yoga" to "임산부 요가",
    "kids_yoga" to "키즈 요가",
    "women_only" to "여성 전용",
    "men_only" to "남성 전용",
    "unisex" to "남녀 공용",
    "pet_yoga" to "펫요가",
)

@Composable
internal fun ClassIntroStepContent(
    title: String,
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
            title = title,
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
internal fun ClassCategoryStepContent(
    title: String,
    categoryCodes: Set<String>,
    onCategoryCodesChange: (Set<String>) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        YogheeHeader(
            title = title,
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

@Composable
internal fun ClassCenterStepContent(
    title: String,
    centersState: CentersState,
    selectedCenterId: String?,
    onCenterSelected: (String) -> Unit,
    onLoadCenters: () -> Unit,
    onBack: () -> Unit,
    onRegisterLocationClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) { onLoadCenters() }

    Column(modifier = modifier.fillMaxSize()) {
        YogheeHeader(
            title = title,
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
                borderColor = LIGHT_GRAY,
                titleColor = BLACK,
                subTitleColor = GRAY
            )
            CentersSection(
                centersState = centersState,
                selectedCenterId = selectedCenterId,
                onCenterSelected = onCenterSelected,
                modifier = Modifier.padding(top = 33.dp),
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun CentersSection(
    centersState: CentersState,
    selectedCenterId: String?,
    onCenterSelected: (String) -> Unit,
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
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                RegisterSectionTitle(title = "요가원 불러오기")
                centersState.centers.forEach { center ->
                    LocationListItem(
                        date = formatCreatedAt(center.createdAt),
                        name = center.name,
                        location = center.address,
                        selected = center.centerId == selectedCenterId,
                        onClick = { onCenterSelected(center.centerId) },
                    )
                }
            }
        }
        is CentersState.Error -> Unit
    }
}

@Composable
internal fun ClassImageStepContent(
    title: String,
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
            title = title,
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

internal val HOLIDAY_DAY_OF_WEEK_OPTIONS = listOf(
    "MON" to "월",
    "TUE" to "화",
    "WED" to "수",
    "THU" to "목",
    "FRI" to "금",
    "SAT" to "토",
    "SUN" to "일",
)

// 하나의 pill이 전송/토글해야 하는 공휴일 코드 묶음.
// 예) "설날 연휴" pill을 누르면 SEOLLAL_PREV/DAY/NEXT 3개 코드가 함께 처리된다.
internal data class HolidayOption(val codes: Set<String>, val label: String)

internal val HOLIDAY_OPTIONS = listOf(
    HolidayOption(setOf("NEW_YEAR_DAY"), "신정"),
    HolidayOption(setOf("SEOLLAL_DAY"), "설날 당일"),
    HolidayOption(setOf("SEOLLAL_PREV", "SEOLLAL_DAY", "SEOLLAL_NEXT"), "설날 연휴"),
    HolidayOption(setOf("INDEPENDENCE_MOVEMENT_DAY"), "삼일절"),
    HolidayOption(setOf("BUDDHA_BIRTHDAY"), "석가탄신일"),
    HolidayOption(setOf("CHILDREN_DAY"), "어린이날"),
    HolidayOption(setOf("MEMORIAL_DAY"), "현충일"),
    HolidayOption(setOf("LIBERATION_DAY"), "광복절"),
    HolidayOption(setOf("NATIONAL_FOUNDATION_DAY"), "개천절"),
    HolidayOption(setOf("HANGEUL_DAY"), "한글날"),
    HolidayOption(setOf("CHUSEOK_DAY"), "추석 당일"),
    HolidayOption(setOf("CHUSEOK_PREV", "CHUSEOK_DAY", "CHUSEOK_NEXT"), "추석 연휴"),
    HolidayOption(setOf("CHRISTMAS_DAY"), "크리스마스"),
)

internal val ALL_HOLIDAY_CODES: Set<String> =
    HOLIDAY_OPTIONS.flatMap { it.codes }.toSet()

@Composable
internal fun ClassHolidayStepContent(
    title: String,
    hasHoliday: Boolean,
    holidayDaysOfWeek: Set<String>,
    holidays: Set<String>,
    onHasHolidayChange: (Boolean) -> Unit,
    onHolidayDayOfWeekToggle: (String) -> Unit,
    onHolidayToggle: (Set<String>) -> Unit,
    onAllHolidayToggle: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        YogheeHeader(
            title = title,
            onBack = onBack,
            subTitle = stringResource(R.string.inquire),
            onSubTitleClick = {},
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
        ) {
            RegisterSectionTitle(
                title = "휴무일이 있나요?",
                modifier = Modifier.padding(top = 20.dp),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp),
            ) {
                HolidayChoiceButton(
                    text = "휴무일이 있어요",
                    selected = hasHoliday,
                    onClick = { onHasHolidayChange(true) },
                    modifier = Modifier.weight(1f),
                )
                HolidayChoiceButton(
                    text = "휴무일이 없어요",
                    selected = !hasHoliday,
                    onClick = { onHasHolidayChange(false) },
                    modifier = Modifier.weight(1f),
                )
            }
            if (hasHoliday) {
                HorizontalDivider(thickness = 1.dp, color = LIGHT_GRAY, modifier = Modifier.padding(top = 24.dp))
                RegisterSectionTitle(
                    title = "휴무요일 선택",
                    modifier = Modifier.padding(top = 20.dp, start = 8.dp),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 33.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    HOLIDAY_DAY_OF_WEEK_OPTIONS.forEach { (code, label) ->
                        HolidayDayOfWeekChip(
                            label = label,
                            selected = code in holidayDaysOfWeek,
                            onClick = { onHolidayDayOfWeekToggle(code) },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
                HorizontalDivider(thickness = 1.dp, color = LIGHT_GRAY, modifier = Modifier.padding(top = 20.dp))
                RegisterSectionTitle(
                    title = "다음 공휴일 중 휴무일이 있나요?",
                    modifier = Modifier.padding(top = 20.dp, start = 8.dp),
                )
                AllHolidayCheckboxRow(
                    checked = holidays == ALL_HOLIDAY_CODES,
                    onClick = onAllHolidayToggle,
                    modifier = Modifier.padding(top = 16.dp, start = 8.dp),
                )
                HolidayOptionsGrid(
                    selected = holidays,
                    onToggle = onHolidayToggle,
                    modifier = Modifier.padding(top = 20.dp, start = 8.dp),
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun HolidayChoiceButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) Green_D6F695 else LIGHT_GRAY)
            .noRippleClickable(onClick),
        contentAlignment = Alignment.Center,
    ) {
        YogheeText(
            text = text,
            color = BLACK,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun AllHolidayCheckboxRow(
    checked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.noRippleClickable(onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(WHITE)
                .border(1.dp, LIGHT_GRAY, CircleShape)
                .padding(5.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (checked) {
                Image(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = "선택됨",
                )
            }
        }
        YogheeText(
            text = "전체 휴무",
            color = BLACK,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun HolidayOptionsGrid(
    selected: Set<String>,
    onToggle: (Set<String>) -> Unit,
    modifier: Modifier = Modifier,
) {
    // 한 행에 4개씩 배치. 마지막 행이 4개 미만이면 남은 자리를 Spacer로 채워 폭을 유지.
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        HOLIDAY_OPTIONS.chunked(4).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                rowItems.forEach { option ->
                    // pill의 모든 코드가 선택돼 있을 때만 시각적으로 selected 처리.
                    val isSelected = option.codes.all { it in selected }
                    HolidayPillButton(
                        label = option.label,
                        selected = isSelected,
                        onClick = { onToggle(option.codes) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HolidayPillButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(30.dp)
            .clip(RoundedCornerShape(33.dp))
            .background(if (selected) Green_D6F695 else LIGHT_GRAY)
            .noRippleClickable(onClick)
            .padding(vertical = 8.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        YogheeText(
            text = label,
            color = BLACK,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun HolidayDayOfWeekChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(horizontal = 15.dp, vertical = 13.dp)
            .noRippleClickable(onClick),
        contentAlignment = Alignment.Center,
    ) {
        YogheeText(
            text = label,
            color = if (selected) GRAY else BLACK,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
        )
        if(selected) {
            Image(
                painter = painterResource(R.drawable.btn_selected_state),
                contentDescription = "선택됨"
            )
        }
    }
}

@Composable
internal fun ClassOperationStepContent(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        YogheeHeader(
            title = title,
            onBack = onBack,
            subTitle = stringResource(R.string.inquire),
            onSubTitleClick = {},
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                YogheeText(
                    text = "수련시간",
                    color = BLACK,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
                YogheeText(
                    text = "한 타임 기준",
                    color = GRAY,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
internal fun StepPlaceholderContent(
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
internal fun RegisterBottomBar(
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
                modifier = Modifier
                    .width(116.dp)
                    .noRippleClickable(onPrevious),
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

internal tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

internal fun Context.openAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", packageName, null)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}
