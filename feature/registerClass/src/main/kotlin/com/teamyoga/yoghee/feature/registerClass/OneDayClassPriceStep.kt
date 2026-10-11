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
    price: String,
    onPriceChange: (String) -> Unit,
    discountEnabled: Boolean,
    onDiscountEnabledChange: (Boolean) -> Unit,
    discountRate: String,
    onDiscountRateChange: (String) -> Unit,
    discountStartMillis: Long?,
    discountEndMillis: Long?,
    onDiscountDateChange: (Long?, Long?) -> Unit,
    refundRate24: String,
    refundRate48: String,
    refundRate72: String,
    onRefundRateChange: (hoursBeforeClass: Int, value: String) -> Unit,
    noticeText: String,
    onNoticeChange: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
            EnterPrice(price = price, onPriceChange = onPriceChange)
            HorizontalDivider(thickness = 1.dp, color = LIGHT_GRAY, modifier = Modifier.padding(vertical = 20.dp))
            DiscountArea(
                enabled = discountEnabled,
                onEnabledChange = onDiscountEnabledChange,
                discountRate = discountRate,
                onDiscountRateChange = onDiscountRateChange,
                startMillis = discountStartMillis,
                endMillis = discountEndMillis,
                onDateChange = onDiscountDateChange,
            )
            HorizontalDivider(thickness = 1.dp, color = LIGHT_GRAY, modifier = Modifier.padding(vertical = 20.dp))

            PriceTitleText(text = "환불기준")
            PriceTitleText(text = "예약 취소 안내 (환급금액)", modifier = Modifier.padding(top = 20.dp))
            EnterPercentPrice(
                title = "수련 시작",
                label = "환불",
                time = 24,
                value = refundRate24,
                onValueChange = { onRefundRateChange(24, it) },
            )
            EnterPercentPrice(
                title = "수련 시작",
                label = "환불",
                time = 48,
                value = refundRate48,
                onValueChange = { onRefundRateChange(48, it) },
            )
            EnterPercentPrice(
                title = "수련 시작",
                label = "환불",
                time = 72,
                value = refundRate72,
                onValueChange = { onRefundRateChange(72, it) },
            )
            HorizontalDivider(thickness = 1.dp, color = LIGHT_GRAY, modifier = Modifier.padding(vertical = 20.dp))

            PriceTitleText(text = "예약 시 안내사항")
            HintTextField(
                value = noticeText,
                onValueChange = onNoticeChange,
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
fun EnterPrice(price: String, onPriceChange: (String) -> Unit) {
    // 1회 수업 가격
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 38.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PriceTitleText(text = "1회수업")
        BasicTextField(
            value = price,
            onValueChange = { input ->
                onPriceChange(input.filter { it.isDigit() }.trimStart('0'))
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
                    if (price.isEmpty()) {
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
fun DiscountArea(
    enabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    discountRate: String,
    onDiscountRateChange: (String) -> Unit,
    startMillis: Long?,
    endMillis: Long?,
    onDateChange: (Long?, Long?) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            PriceTitleText(text = "할인 적용")
            Spacer(modifier = Modifier.weight(1f))
            YogheeToggle(
                checked = enabled,
                onCheckedChange = onEnabledChange,
            )
        }
        if (enabled) {
            EnterPercentPrice(
                title = "할인률 기준",
                label = "할인",
                value = discountRate,
                onValueChange = onDiscountRateChange,
                modifier = Modifier.padding(top = 24.dp),
            )
            EnterDiscountDate(
                title = "할인 적용 기간",
                label = "까지",
                startMillis = startMillis,
                endMillis = endMillis,
                onDateChange = onDateChange,
            )
        }
    }
}

@Composable
fun EnterPercentPrice(
    title: String,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    time: Int? = null,
    modifier: Modifier = Modifier.padding(top = 16.dp),
) {
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
            value = value,
            onValueChange = { input ->
                onValueChange(input.filter { it.isDigit() }.trimStart('0'))
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
fun EnterDiscountDate(
    title: String,
    label: String,
    startMillis: Long?,
    endMillis: Long?,
    onDateChange: (Long?, Long?) -> Unit,
) {
    // 다이얼로그 열림 여부는 순수 UI 상태라서 로컬에 보관.
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    val displayText = when {
        startMillis == null -> "시작일 ~ 종료일"
        endMillis == null -> "${formatDateMillis(startMillis)} ~ 종료일"
        else -> "${formatDateMillis(startMillis)} ~ ${formatDateMillis(endMillis)}"
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
                    onDateChange(
                        rangeState.selectedStartDateMillis,
                        rangeState.selectedEndDateMillis,
                    )
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
            Step6Content(
                price = "15000",
                onPriceChange = {},
                discountEnabled = true,
                onDiscountEnabledChange = {},
                discountRate = "10",
                onDiscountRateChange = {},
                discountStartMillis = null,
                discountEndMillis = null,
                onDiscountDateChange = { _, _ -> },
                refundRate24 = "50",
                refundRate48 = "80",
                refundRate72 = "100",
                onRefundRateChange = { _, _ -> },
                noticeText = "환불은 수련 시작 24시간 전까지 가능합니다.",
                onNoticeChange = {},
                onBack = {},
            )
        }
    }
}
