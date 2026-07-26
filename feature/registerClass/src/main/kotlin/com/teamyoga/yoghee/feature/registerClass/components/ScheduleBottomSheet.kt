package com.teamyoga.yoghee.feature.registerClass.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.GRAY
import com.teamyoga.yoghee.core.ui.theme.LIGHT_GRAY
import com.teamyoga.yoghee.core.ui.theme.MIND_ORANGE
import com.teamyoga.yoghee.core.ui.theme.WHITE
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme
import com.teamyoga.yoghee.core.ui.util.noRippleClickable

data class ScheduleInput(
    val startTime: String,
    val endTime: String,
    val className: String,
    val minCount: Int,
)

private const val MIN_COUNT = 0
private const val MAX_COUNT = 999
private const val DEFAULT_TIME = "00:00"

private enum class TimePickerTarget { START, END }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleBottomSheet(
    onDismiss: () -> Unit,
    onApply: (ScheduleInput) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var startTime by remember { mutableStateOf(DEFAULT_TIME) }
    var endTime by remember { mutableStateOf(DEFAULT_TIME) }
    var className by remember { mutableStateOf("") }
    var minCount by remember { mutableIntStateOf(MIN_COUNT) }
    var pickerTarget by remember { mutableStateOf<TimePickerTarget?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = WHITE,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                TimeBox(
                    label = "시작시간",
                    time = startTime,
                    onClick = { pickerTarget = TimePickerTarget.START },
                    modifier = Modifier.weight(1f),
                )
                TimeBox(
                    label = "종료시간",
                    time = endTime,
                    onClick = { pickerTarget = TimePickerTarget.END },
                    modifier = Modifier.weight(1f),
                )
            }
            LabeledTextField(
                label = "개별 수련명*",
                value = className,
                onValueChange = { className = it },
            )
            CounterRow(
                label = "최소 수련 가능인원",
                count = minCount,
                onDecrement = { if (minCount > MIN_COUNT) minCount-- },
                onIncrement = { if (minCount < MAX_COUNT) minCount++ },
            )
            Spacer(modifier = Modifier.height(8.dp))
            ApplyButton(
                modifier = Modifier.align(Alignment.End),
                onClick = {
                    onApply(ScheduleInput(startTime, endTime, className, minCount))
                    onDismiss()
                },
            )
        }
    }

    pickerTarget?.let { target ->
        val current = if (target == TimePickerTarget.START) startTime else endTime
        TimePickerDialog(
            initialTime = current,
            onDismiss = { pickerTarget = null },
            onConfirm = { hour, minute ->
                val formatted = "%02d:%02d".format(hour, minute)
                if (target == TimePickerTarget.START) startTime = formatted else endTime = formatted
                pickerTarget = null
            },
        )
    }
}

@Composable
private fun TimeBox(
    label: String,
    time: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(49.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, GRAY, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        YogheeText(
            text = buildAnnotatedString {
                append(label)
                withStyle(SpanStyle(color = MIND_ORANGE)) {
                    append(" * (24시간 기준)")
                }
            },
            color = GRAY,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
        )
        YogheeText(
            text = time,
            color = BLACK,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(top = 4.dp)
                .noRippleClickable(onClick),
        )
    }
}

@Composable
private fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        YogheeText(
            text = label,
            color = GRAY,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            textStyle = androidx.compose.ui.text.TextStyle(
                color = BLACK,
                fontSize = 14.sp,
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
            ),
        )
    }
}

@Composable
private fun CounterRow(
    label: String,
    count: Int,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        YogheeText(
            text = label,
            color = BLACK,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f),
        )
        Row(modifier = Modifier.width(94.dp),
            verticalAlignment = Alignment.CenterVertically) {
            CounterButton(text = "-", enabled = count > MIN_COUNT, onClick = onDecrement)
            YogheeText(
                text = count.toString(),
                color = BLACK,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            CounterButton(text = "+", enabled = count < MAX_COUNT, onClick = onIncrement)
        }
    }
}

@Composable
private fun CounterButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(LIGHT_GRAY)
            .then(if (enabled) Modifier.noRippleClickable(onClick) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        YogheeText(
            text = text,
            color = if (enabled) BLACK else GRAY,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ApplyButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(208.dp)
            .height(48.dp)
            .paint(
                painter = painterResource(R.drawable.btn_continue_class_register),
                contentScale = ContentScale.FillBounds,
            )
            .noRippleClickable(onClick),
        contentAlignment = Alignment.Center,
    ) {
        YogheeText(
            text = "적용",
            color = BLACK,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    initialTime: String,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit,
) {
    val parts = initialTime.split(":")
    val initialHour = parts.getOrNull(0)?.toIntOrNull() ?: 0
    val initialMinute = parts.getOrNull(1)?.toIntOrNull() ?: 0
    val state = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true,
    )

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .background(WHITE, RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TimePicker(state = state)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(onClick = onDismiss) {
                    YogheeText(text = "취소", color = GRAY, fontSize = 14.sp)
                }
                TextButton(onClick = { onConfirm(state.hour, state.minute) }) {
                    YogheeText(text = "확인", color = BLACK, fontSize = 14.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "ScheduleBottomSheet")
@Composable
private fun ScheduleBottomSheetPreview() {
    YogheeTheme {
        Column(
            modifier = Modifier
                .background(WHITE)
                .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                TimeBox(
                    label = "시작시간",
                    time = "09:00",
                    onClick = {},
                    modifier = Modifier.weight(1f),
                )
                TimeBox(
                    label = "종료시간",
                    time = "10:30",
                    onClick = {},
                    modifier = Modifier.weight(1f),
                )
            }
            LabeledTextField(
                label = "개별 수련명*",
                value = "",
                onValueChange = {},
            )
            CounterRow(
                label = "최소 수련 가능인원",
                count = 3,
                onDecrement = {},
                onIncrement = {},
            )
            Spacer(modifier = Modifier.height(8.dp))
            ApplyButton(onClick = {})
        }
    }
}
