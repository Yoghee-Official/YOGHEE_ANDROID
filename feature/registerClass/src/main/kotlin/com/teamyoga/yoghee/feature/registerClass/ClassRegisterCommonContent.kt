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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.ceil
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.teamyoga.yoghee.core.common.formatCreatedAt
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.component.YogheeHeader
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.FLOW_BLUE
import com.teamyoga.yoghee.core.ui.theme.GRAY
import com.teamyoga.yoghee.core.ui.theme.Green_D6F695
import com.teamyoga.yoghee.core.ui.theme.LAND_BROWN
import com.teamyoga.yoghee.core.ui.theme.LIGHT_GRAY
import com.teamyoga.yoghee.core.ui.theme.MIND_ORANGE
import com.teamyoga.yoghee.core.ui.theme.SAND_BEIGE
import com.teamyoga.yoghee.core.ui.theme.WHITE
import com.teamyoga.yoghee.core.ui.util.noRippleClickable
import com.teamyoga.yoghee.feature.registerClass.components.ClassIntroductionSection
import com.teamyoga.yoghee.feature.registerClass.components.ClassPurposeSection
import com.teamyoga.yoghee.feature.registerClass.components.ClassSchedule
import com.teamyoga.yoghee.feature.registerClass.components.ImageItem
import com.teamyoga.yoghee.feature.registerClass.components.ImagePickerGrid
import com.teamyoga.yoghee.feature.registerClass.components.ImageSource
import com.teamyoga.yoghee.feature.registerClass.components.ImageSourcePickerBottomSheet
import com.teamyoga.yoghee.feature.registerClass.components.LocationListItem
import com.teamyoga.yoghee.feature.registerClass.components.LocationRegisterButton
import com.teamyoga.yoghee.feature.registerClass.components.MultiSelectChipsSection
import com.teamyoga.yoghee.feature.registerClass.components.RegisterSectionTitle
import com.teamyoga.yoghee.feature.registerClass.components.RegularScheduleBottomSheet

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

// --- ScheduleGrid 상수 ---
// 컬럼(1시간)의 시각적 콘텐츠 폭.
private val GRID_COLUMN_WIDTH = 36.dp
// 컬럼 사이 간격.
private val GRID_COLUMN_SPACING = 76.dp
// 시간당 pitch. 카드 위치/폭 계산의 기준.
private val GRID_HOUR_PITCH = 112.dp
// 시간 헤더 영역 높이.
private val GRID_TIME_HEADER_HEIGHT = 50.dp
// 요일 행 콘텐츠 높이(카드가 채우는 영역, 칩 위치).
private val GRID_ROW_HEIGHT = 58.dp
// 요일 행 하단 여백. 다음 행과의 시각적 간격.
private val GRID_ROW_BOTTOM_PADDING = 12.dp
// 요일 행 간 stride(= ROW_HEIGHT + BOTTOM_PADDING). 오버레이 좌표 계산 기준.
private val GRID_ROW_STRIDE = GRID_ROW_HEIGHT + GRID_ROW_BOTTOM_PADDING
// 오버레이(카드/+버튼)의 X 시작 위치.
// 이론상: start padding 8dp + 내부 컬럼 폭 28dp / 2 = 22dp
// 실제 렌더링에서는 stroke 안티에일리어싱/서브픽셀 반올림으로 카드가 선보다 살짝 왼쪽에
// 보이는 경향이 있어, +2dp 시각 튜닝을 반영해 24dp 사용.
private val GRID_LINE_OFFSET_IN_COLUMN = 24.dp
// + 버튼 지름.
private val GRID_ADD_BUTTON_SIZE = 20.dp
private const val GRID_TOTAL_HOURS = 24
private const val GRID_INITIAL_HOUR = 6

private val GRID_TOTAL_WIDTH = GRID_HOUR_PITCH * GRID_TOTAL_HOURS
private val GRID_TOTAL_HEIGHT =
    GRID_TIME_HEADER_HEIGHT + GRID_ROW_STRIDE * HOLIDAY_DAY_OF_WEEK_OPTIONS.size

// "HH:MM" → 자정 이후 총 분 수.
private fun parseTimeToMinutes(time: String): Int? {
    val parts = time.split(":")
    if (parts.size != 2) return null
    val h = parts[0].toIntOrNull() ?: return null
    val m = parts[1].toIntOrNull() ?: return null
    return h * 60 + m
}

// (dayCode, hour) 셀을 점유하는 스케줄을 반환. 시(hour) 셀 범위 [hour*60, (hour+1)*60) 와 오버랩 판정.
private fun findCoveringSchedule(
    schedules: List<ScheduleEntry>,
    dayCode: String,
    hour: Int,
): ClassSchedule? {
    val hourStart = hour * 60
    val hourEnd = (hour + 1) * 60
    for (entry in schedules) {
        if (entry.dayCode != dayCode) continue
        val start = parseTimeToMinutes(entry.schedule.startTime) ?: continue
        val end = parseTimeToMinutes(entry.schedule.endTime) ?: continue
        if (start >= end) continue
        if (start < hourEnd && hourStart < end) return entry.schedule
    }
    return null
}

@Composable
private fun ScheduleCard(
    schedule: ClassSchedule,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(FLOW_BLUE)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        YogheeText(
            text = "${schedule.startTime} ~ ${schedule.endTime}",
            color = BLACK,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
        YogheeText(
            text = schedule.className,
            color = BLACK,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
        )
        if (schedule.instructorMemo.isNotBlank()) {
            YogheeText(
                text = schedule.instructorMemo,
                color = BLACK,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
            )
        }
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
    holidayDaysOfWeek: Set<String>,
    schedules: List<ScheduleEntry>,
    onAddSchedule: (dayCode: String, schedule: ClassSchedule) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // + 버튼 클릭 시 (dayCode, hour) 저장 → 이 값이 not null이면 바텀시트 노출.
    var pendingSchedule by remember { mutableStateOf<Pair<String, Int>?>(null) }

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
                .padding(start = 24.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 38.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                YogheeText(
                    text = "수련 시간",
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
            ScheduleGrid(
                holidayDaysOfWeek = holidayDaysOfWeek,
                schedules = schedules,
                onAddSchedule = { dayCode, hour -> pendingSchedule = dayCode to hour },
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    pendingSchedule?.let { (dayCode, hour) ->
        // 클릭한 셀의 시간을 시작시간으로, 다음 정시를 종료시간으로 초기 노출.
        val initial = ClassSchedule(
            startTime = "%02d:00".format(hour),
            endTime = "%02d:00".format((hour + 1) % 24),
            className = "",
            minCount = 0,
            maxCount = 0,
        )
        // 시간 오버랩 감지에 사용할, 같은 요일의 기존 스케줄만 추려서 전달.
        val existingDaySchedules = schedules
            .filter { it.dayCode == dayCode }
            .map { it.schedule }
        RegularScheduleBottomSheet(
            initial = initial,
            existingDaySchedules = existingDaySchedules,
            onDismiss = { pendingSchedule = null },
            onApply = { schedule ->
                onAddSchedule(dayCode, schedule)
                pendingSchedule = null
            },
        )
    }
}

/**
 * 시간표 그리드.
 *
 * 구조:
 * - 좌측 Y축(요일 라벨) sticky
 * - 우측: horizontalScroll Box 안에 두 레이어
 *   1) 베이스 그리드: 24개 HourColumn (시간 헤더 + 세로 선 + `+` 버튼)
 *   2) 스케줄 카드 오버레이: 컴포지션 순서상 뒤에 나와 자연스레 위에 그려짐
 *
 * 카드는 개별 컬럼에 종속되지 않고 `offset(x, y)`로 절대 배치되므로,
 * 시작 컬럼이 뷰포트를 벗어나도 유지되고, zIndex/requiredWidth 트릭이 필요 없다.
 */
@Composable
private fun ScheduleGrid(
    holidayDaysOfWeek: Set<String>,
    schedules: List<ScheduleEntry>,
    onAddSchedule: (dayCode: String, hour: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val hourPitchPx = with(density) { GRID_HOUR_PITCH.toPx() }
    val scrollState = rememberScrollState(
        initial = with(density) { (GRID_HOUR_PITCH * GRID_INITIAL_HOUR).toPx() }.toInt(),
    )
    val activeHour by remember(hourPitchPx) {
        derivedStateOf {
            ceil(scrollState.value / hourPitchPx).toInt().coerceIn(0, GRID_TOTAL_HOURS - 1)
        }
    }

    Row(modifier = modifier.fillMaxWidth()) {
        YAxis(holidayDaysOfWeek = holidayDaysOfWeek)    // 요일 sticky 영역
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
        ) {
            Box(
                modifier = Modifier
                    .width(GRID_TOTAL_WIDTH)
                    .height(GRID_TOTAL_HEIGHT),
            ) {
                // 레이어 순서 = 그리기 순서. 뒤에 나온 것이 위에 렌더된다.
                // 1) 베이스 그리드(시간 헤더 + 세로 선) → 2) 카드 → 3) + 버튼
                BaseGrid(activeHour = activeHour)
                SchedulesOverlay(schedules = schedules)
                AddButtonsOverlay(
                    activeHour = activeHour,
                    holidayDaysOfWeek = holidayDaysOfWeek,
                    schedules = schedules,
                    onAddSchedule = onAddSchedule,
                )
            }
        }
    }
}

@Composable
private fun YAxis(holidayDaysOfWeek: Set<String>) {
    Column {
        Spacer(modifier = Modifier.height(GRID_TIME_HEADER_HEIGHT))
        HOLIDAY_DAY_OF_WEEK_OPTIONS.forEach { (code, label) ->
            Box(
                modifier = Modifier
                    .padding(bottom = GRID_ROW_BOTTOM_PADDING)
                    .height(GRID_ROW_HEIGHT),
                contentAlignment = Alignment.Center,
            ) {
                HolidayDayOfWeekChip(
                    label = label,
                    selected = code in holidayDaysOfWeek,
                    onClick = {},
                )
            }
        }
    }
}

@Composable
private fun BaseGrid(activeHour: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(GRID_COLUMN_SPACING)) {
        (0 until GRID_TOTAL_HOURS).forEach { hour ->
            HourColumn(hour = hour, isActive = hour == activeHour)
        }
    }
}

@Composable
private fun HourColumn(hour: Int, isActive: Boolean) {
    Column(modifier = Modifier.width(GRID_COLUMN_WIDTH)) {
        TimeHeader(hour = hour)
        // 요일 셀 iteration 없이 단일 Box. 세로 선만 그리고, + 버튼과 카드는 오버레이가 담당한다.
        Box(
            modifier = Modifier
                .padding(start = 8.dp)
                .fillMaxWidth()
                .height(GRID_ROW_STRIDE * HOLIDAY_DAY_OF_WEEK_OPTIONS.size)
                .drawBehind { drawVerticalLine(isActive) },
        )
    }
}

@Composable
private fun TimeHeader(hour: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(GRID_TIME_HEADER_HEIGHT)
            .padding(top = 12.dp),
        contentAlignment = Alignment.TopEnd,
    ) {
        YogheeText(
            text = "%02d:00".format(hour),
            color = GRAY,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun AddScheduleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    YogheeText(
        text = "+",
        color = WHITE,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = modifier
            .size(GRID_ADD_BUTTON_SIZE)
            .clip(CircleShape)
            .background(MIND_ORANGE)
            .noRippleClickable(onClick),
    )
}

private fun DrawScope.drawVerticalLine(isActive: Boolean) {
    val lineColor = if (isActive) MIND_ORANGE else LIGHT_GRAY
    val centerX = size.width / 2f
    val dotRadius = 3.dp.toPx()
    val topY = dotRadius
    val bottomY = size.height - dotRadius
    drawCircle(color = lineColor, radius = dotRadius, center = Offset(centerX, topY))
    drawLine(
        color = lineColor,
        start = Offset(centerX, topY),
        end = Offset(centerX, bottomY),
        strokeWidth = 1.dp.toPx(),
    )
    drawCircle(color = lineColor, radius = dotRadius, center = Offset(centerX, bottomY))
}

/**
 * 스케줄 카드 오버레이. 각 카드는 absolute offset(x, y) 로 배치되어
 * 컬럼 종속에서 자유롭고 뷰포트 스크롤과 독립적으로 좌표가 유지된다.
 *
 * cardY = TIME_HEADER + ROW_STRIDE × dayIndex (Y축 요일 행 stride와 일치).
 */
@Composable
private fun SchedulesOverlay(schedules: List<ScheduleEntry>) {
    val pitchPerMinute = GRID_HOUR_PITCH.value / 60f
    schedules.forEach { entry ->
        val startMin = parseTimeToMinutes(entry.schedule.startTime) ?: return@forEach
        val endMin = parseTimeToMinutes(entry.schedule.endTime) ?: return@forEach
        if (startMin >= endMin) return@forEach
        val dayIndex = HOLIDAY_DAY_OF_WEEK_OPTIONS.indexOfFirst { it.first == entry.dayCode }
        if (dayIndex < 0) return@forEach

        val cardX = (startMin * pitchPerMinute).dp + GRID_LINE_OFFSET_IN_COLUMN
        val cardY = GRID_TIME_HEADER_HEIGHT + GRID_ROW_STRIDE * dayIndex
        val cardWidth = ((endMin - startMin) * pitchPerMinute).dp
        ScheduleCard(
            schedule = entry.schedule,
            modifier = Modifier
                .offset(x = cardX, y = cardY)
                .width(cardWidth)
                .height(GRID_ROW_HEIGHT),
        )
    }
}

/**
 * + 버튼 오버레이. SchedulesOverlay 뒤에 놓여 카드 위로 그려진다.
 * 활성 시(activeHour) × 비휴무 요일 × 이미 커버 중인 스케줄 없음 셀에만 노출.
 */
@Composable
private fun AddButtonsOverlay(
    activeHour: Int,
    holidayDaysOfWeek: Set<String>,
    schedules: List<ScheduleEntry>,
    onAddSchedule: (String, Int) -> Unit,
) {
    // + 버튼을 각 셀 콘텐츠 영역(ROW_HEIGHT) 정중앙에 위치시키기 위한 오프셋.
    val buttonYWithinCell = (GRID_ROW_HEIGHT - GRID_ADD_BUTTON_SIZE) / 2
    val buttonXOffset = GRID_LINE_OFFSET_IN_COLUMN - GRID_ADD_BUTTON_SIZE / 2

    HOLIDAY_DAY_OF_WEEK_OPTIONS.forEachIndexed { dayIndex, (dayCode, _) ->
        if (dayCode in holidayDaysOfWeek) return@forEachIndexed
        // 이미 활성 시간(activeHour)에 스케줄이 등록된 요일은 + 버튼 미노출.
        if (findCoveringSchedule(schedules, dayCode, activeHour) != null) return@forEachIndexed

        val buttonX = GRID_HOUR_PITCH * activeHour + buttonXOffset
        val buttonY = GRID_TIME_HEADER_HEIGHT + GRID_ROW_STRIDE * dayIndex + buttonYWithinCell
        AddScheduleButton(
            onClick = { onAddSchedule(dayCode, activeHour) },
            modifier = Modifier.offset(x = buttonX, y = buttonY),
        )
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
