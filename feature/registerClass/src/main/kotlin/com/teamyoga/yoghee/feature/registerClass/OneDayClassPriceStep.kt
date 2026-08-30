package com.teamyoga.yoghee.feature.registerClass

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.component.YogheeHeader
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.component.YogheeToggle
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.FLOW_BLUE
import com.teamyoga.yoghee.core.ui.theme.GRAY
import com.teamyoga.yoghee.core.ui.theme.Green_D6F695
import com.teamyoga.yoghee.core.ui.theme.LAND_BROWN
import com.teamyoga.yoghee.core.ui.theme.LIGHT_GRAY
import com.teamyoga.yoghee.core.ui.theme.MIND_ORANGE
import com.teamyoga.yoghee.core.ui.theme.SAND_BEIGE
import com.teamyoga.yoghee.core.ui.theme.WHITE
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme
import com.teamyoga.yoghee.core.ui.util.noRippleClickable
import com.teamyoga.yoghee.feature.registerClass.components.HintTextField
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
internal fun Step6Content(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var noticeText by rememberSaveable { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        YogheeHeader(
            title = stringResource(R.string.one_day_class_register_step6_title),
            onBack = onBack,
            subTitle = stringResource(R.string.inquire),
            onSubTitleClick = {},
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
        ) {
            EnterPrice()
            HorizontalDivider(thickness = 1.dp, color = LIGHT_GRAY, modifier = Modifier.padding(vertical = 20.dp))
            DiscountArea()
            HorizontalDivider(thickness = 1.dp, color = LIGHT_GRAY, modifier = Modifier.padding(vertical = 20.dp))

            PriceTitleText(text = "환불기준")
            PriceTitleText(text = "예약 취소 안내 (환급금액)", modifier = Modifier.padding(top = 20.dp))
            EnterPercentPrice("수련 시작", "환불", 24)
            EnterPercentPrice("수련 시작", "환불", 48)
            EnterPercentPrice("수련 시작", "환불", 72)
            HorizontalDivider(thickness = 1.dp, color = LIGHT_GRAY, modifier = Modifier.padding(vertical = 20.dp))

            PriceTitleText(text = "예약 시 안내사항")
            HintTextField(
                value = noticeText,
                onValueChange = { noticeText = it },
                hint1 = "내용",
                hint2 = "입금이나 환불과 관련하여, 추가로 안내할 사항을 입력하세요.",
                maxLength = 3000,
                modifier = Modifier.padding(top = 10.dp)
            )
            YogheeText(
                text = "미리보기",
                color = BLACK,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(FLOW_BLUE, Green_D6F695),
                        ),
                    )
                    .padding(vertical = 13.dp)
            )
        }
    }
}

@Composable
fun EnterPrice() {
    var priceRaw by rememberSaveable { mutableStateOf("") }
    // 1회 수업 가격
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 38.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PriceTitleText(text = "1회수업")
        BasicTextField(
            value = priceRaw,
            onValueChange = { input ->
                priceRaw = input.filter { it.isDigit() }.trimStart('0')
            },
            modifier = Modifier.weight(1f),
            textStyle = TextStyle(
                color = BLACK,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = ThousandsSeparatorTransformation,
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.CenterEnd) {
                    if (priceRaw.isEmpty()) {
                        YogheeText(
                            text = "________",
                            color = BLACK,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                        )
                    }
                    innerTextField()
                }
            },
        )
        PriceTitleText(text = "원")
    }
}

@Composable
fun DiscountArea() {
    var discountEnabled by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            PriceTitleText(text = "할인 적용")
            Spacer(modifier = Modifier.weight(1f))
            YogheeToggle(
                checked = discountEnabled,
                onCheckedChange = { discountEnabled = it },
            )
        }
        if (discountEnabled) {
            EnterPercentPrice("할인률 기준", "할인", modifier = Modifier.padding(top = 24.dp))
            EnterDiscountDate("할인 적용 기간", "까지")
        }
    }
}

@Composable
fun EnterPercentPrice(
    title: String,
    label: String,
    time: Int? = null,
    modifier: Modifier = Modifier.padding(top = 16.dp)
) {
    var text by rememberSaveable { mutableStateOf("") }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PriceMediumText(text = title)
        if (time != null) {
            YogheeText(
                text = time.toString(),
                color = MIND_ORANGE,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            PriceMediumText("시간 전")
        }
        BasicTextField(
            value = text,
            onValueChange = { input ->
                text = input.filter { it.isDigit() }.trimStart('0')
            },
            modifier = Modifier.weight(1f),
            textStyle = TextStyle(
                color = MIND_ORANGE,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
        )
        YogheeText(
            text = "%",
            color = MIND_ORANGE,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
        )
        PriceMediumText(text = label, modifier = Modifier.padding(start = 20.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterDiscountDate(title: String, label: String) {
    var startMillis by rememberSaveable { mutableStateOf<Long?>(null) }
    var endMillis by rememberSaveable { mutableStateOf<Long?>(null) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    val displayText = when {
        startMillis == null -> "시작일 ~ 종료일"
        endMillis == null -> "${formatDateMillis(startMillis!!)} ~ 종료일"
        else -> "${formatDateMillis(startMillis!!)} ~ ${formatDateMillis(endMillis!!)}"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PriceMediumText(text = title)
        YogheeText(
            text = displayText,
            color = MIND_ORANGE,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            maxLines = 1,
            modifier = Modifier
                .weight(1f)
                .noRippleClickable { showDatePicker = true },
        )
        PriceMediumText(text = label, modifier = Modifier.padding(start = 20.dp))
    }

    if (showDatePicker) {
        val rangeState = rememberDateRangePickerState(
            initialSelectedStartDateMillis = startMillis,
            initialSelectedEndDateMillis = endMillis,
        )
        val pickerColors = brandDatePickerColors()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    startMillis = rangeState.selectedStartDateMillis
                    endMillis = rangeState.selectedEndDateMillis
                    showDatePicker = false
                }) {
                    Text("확인", color = LAND_BROWN)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("취소", color = GRAY)
                }
            },
            colors = pickerColors,
        ) {
            DateRangePicker(
                state = rangeState,
                title = null,
                headline = null,
                showModeToggle = false,
                colors = pickerColors,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun brandDatePickerColors() = DatePickerDefaults.colors(
    containerColor = WHITE,
    titleContentColor = BLACK,
    headlineContentColor = BLACK,
    weekdayContentColor = GRAY,
    subheadContentColor = BLACK,
    yearContentColor = BLACK,
    currentYearContentColor = MIND_ORANGE,
    selectedYearContentColor = WHITE,
    selectedYearContainerColor = MIND_ORANGE,
    dayContentColor = BLACK,
    selectedDayContentColor = WHITE,
    selectedDayContainerColor = MIND_ORANGE,
    todayContentColor = MIND_ORANGE,
    todayDateBorderColor = MIND_ORANGE,
    dayInSelectionRangeContainerColor = LIGHT_GRAY,
    dayInSelectionRangeContentColor = BLACK,
)

// DatePicker가 반환하는 UTC millis를 로컬 tz 오프셋 영향 없이 "yyyy.MM.dd"로 포맷.
private fun formatDateMillis(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    return formatter.format(Date(millis))
}

@Composable
fun PriceTitleText(text: String, modifier: Modifier = Modifier) {
    YogheeText(
        text = text,
        color = BLACK,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

@Composable
fun PriceMediumText(text: String, modifier: Modifier = Modifier) {
    YogheeText(
        text = text,
        color = BLACK,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        modifier = modifier
    )
}

// 숫자 문자열에 천 단위 콤마를 추가하는 VisualTransformation.
private object ThousandsSeparatorTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text
        val formatted = if (digits.isEmpty()) {
            ""
        } else {
            digits.reversed().chunked(3).joinToString(",").reversed()
        }

        // 커서 위치를 원본 문자열 화면 표시 문자열 사이에서 정확히 맞춰주는 매핑
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (digits.isEmpty()) return 0
                val totalCommas = (digits.length - 1) / 3
                val digitsToRight = digits.length - offset
                val commasToRight = if (digitsToRight > 0) (digitsToRight - 1) / 3 else 0
                return offset + (totalCommas - commasToRight)
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 0) return 0
                if (offset >= formatted.length) return digits.length
                return formatted.take(offset).count { it != ',' }
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}

@Preview(showBackground = true, name = "Step6Content")
@Composable
private fun Step6ContentPreview() {
    YogheeTheme {
        Box(modifier = Modifier.background(SAND_BEIGE)) {
            Step6Content(onBack = {})
        }
    }
}
