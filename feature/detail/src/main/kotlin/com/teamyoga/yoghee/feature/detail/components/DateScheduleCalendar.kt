package com.teamyoga.yoghee.feature.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.Green_D6F695
import com.teamyoga.yoghee.core.ui.theme.Green_F1FFD4
import com.teamyoga.yoghee.core.ui.theme.LIGHT_GRAY
import com.teamyoga.yoghee.core.ui.theme.MIND_ORANGE
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme
import com.teamyoga.yoghee.core.ui.util.noRippleClickable
import java.util.Calendar

data class ScheduleDate(
    val year: Int,
    val month: Int, // 1 ~ 12
    val day: Int,
)

private val WEEKDAY_LABELS = listOf("일", "월", "화", "수", "목", "금", "토")

/**
 * 수업 있는 날짜만 클릭 가능한 캘린더.
 * - 수업 있는 날 : 초록 그라데이션 원
 * - 선택된 날   : 초록 그라데이션 원 + MIND_ORANGE 1dp 테두리
 * - 월 네비게이션 범위 밖(min~max)에서는 화살표를 회색으로 비활성화.
 */
@Composable
fun DateScheduleCalendar(
    scheduleDates: Set<ScheduleDate>,
    selected: ScheduleDate?,
    onDateClick: (ScheduleDate) -> Unit,
    minYear: Int,
    minMonth: Int,
    maxYear: Int,
    maxMonth: Int,
    initialYear: Int,
    initialMonth: Int,
    modifier: Modifier = Modifier,
) {
    var year by remember { mutableIntStateOf(initialYear) }
    var month by remember { mutableIntStateOf(initialMonth) }

    val canGoPrev = year > minYear || (year == minYear && month > minMonth)
    val canGoNext = year < maxYear || (year == maxYear && month < maxMonth)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(color = LIGHT_GRAY, shape = RoundedCornerShape(8.dp))
            .padding(vertical = 16.dp, horizontal = 13.dp),
    ) {
        CalendarHeader(
            year = year,
            month = month,
            canGoPrev = canGoPrev,
            canGoNext = canGoNext,
            onPrevious = {
                if (canGoPrev) {
                    if (month == 1) {
                        year -= 1
                        month = 12
                    } else {
                        month -= 1
                    }
                }
            },
            onNext = {
                if (canGoNext) {
                    if (month == 12) {
                        year += 1
                        month = 1
                    } else {
                        month += 1
                    }
                }
            },
        )
        WeekdayHeader()
        CalendarGrid(
            year = year,
            month = month,
            scheduleDates = scheduleDates,
            selected = selected,
            onDateClick = onDateClick,
        )
    }
}

@Composable
private fun CalendarHeader(
    year: Int,
    month: Int,
    canGoPrev: Boolean,
    canGoNext: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 16.dp, top = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        YogheeText(
            text = year.toString(),
            color = BLACK,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
        )
        YogheeText(
            text = "◀",
            color = if (canGoPrev) BLACK else LIGHT_GRAY,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.noRippleClickable(onPrevious),
        )
        YogheeText(
            text = "${month}월",
            color = BLACK,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 30.dp),
        )
        YogheeText(
            text = "▶",
            color = if (canGoNext) BLACK else LIGHT_GRAY,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.noRippleClickable(onNext),
        )
    }
}

@Composable
private fun WeekdayHeader(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 18.dp, bottom = 12.dp),
    ) {
        WEEKDAY_LABELS.forEach { label ->
            Box(
                modifier = Modifier.weight(1f),
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
    }
}

@Composable
private fun CalendarGrid(
    year: Int,
    month: Int,
    scheduleDates: Set<ScheduleDate>,
    selected: ScheduleDate?,
    onDateClick: (ScheduleDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val calendar = remember(year, month) {
        Calendar.getInstance().apply {
            clear()
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, 1)
        }
    }
    val firstDayOffset = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val totalCells = firstDayOffset + daysInMonth
    val rowCount = (totalCells + 6) / 7

    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        repeat(rowCount) { row ->
            Row(modifier = Modifier.fillMaxWidth()) {
                repeat(7) { col ->
                    val cellIndex = row * 7 + col
                    val day = cellIndex - firstDayOffset + 1
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (day in 1..daysInMonth) {
                            val date = ScheduleDate(year, month, day)
                            val hasSchedule = date in scheduleDates
                            DateCell(
                                day = day,
                                hasSchedule = hasSchedule,
                                isSelected = date == selected,
                                onClick = { if (hasSchedule) onDateClick(date) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DateCell(
    day: Int,
    hasSchedule: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // 셀 모양: 수업 있는 날은 그라데이션, 선택된 날은 그 위에 MIND_ORANGE 테두리를 덧씌운다.
    val baseModifier = when {
        hasSchedule -> Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(Green_D6F695, Green_F1FFD4),
                ),
            )
        else -> Modifier.size(28.dp)
    }
    val borderModifier = if (isSelected) {
        Modifier.border(width = 2.dp, color = MIND_ORANGE, shape = CircleShape)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .then(baseModifier)
            .then(borderModifier)
            .noRippleClickable(onClick),
        contentAlignment = Alignment.Center,
    ) {
        YogheeText(
            text = day.toString(),
            color = BLACK,
            fontSize = 12.sp,
            fontWeight = if (hasSchedule) FontWeight.Bold else FontWeight.Medium,
        )
    }
}

@Preview(showBackground = true, name = "DateScheduleCalendar")
@Composable
private fun DateScheduleCalendarPreview() {
    val schedules = setOf(
        ScheduleDate(2026, 10, 5),
        ScheduleDate(2026, 10, 13),
        ScheduleDate(2026, 10, 15),
        ScheduleDate(2026, 10, 20),
        ScheduleDate(2026, 10, 25),
    )
    var selected by remember { mutableStateOf<ScheduleDate?>(ScheduleDate(2026, 10, 13)) }
    YogheeTheme {
        DateScheduleCalendar(
            scheduleDates = schedules,
            selected = selected,
            onDateClick = { selected = it },
            minYear = 2025,
            minMonth = 10,
            maxYear = 2027,
            maxMonth = 10,
            initialYear = 2026,
            initialMonth = 10,
            modifier = Modifier.padding(vertical = 12.dp),
        )
    }
}
