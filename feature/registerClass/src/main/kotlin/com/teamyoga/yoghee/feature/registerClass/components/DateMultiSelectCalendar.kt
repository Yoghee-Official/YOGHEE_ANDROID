package com.teamyoga.yoghee.feature.registerClass.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
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
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme
import com.teamyoga.yoghee.core.ui.util.noRippleClickable
import java.util.Calendar

data class CalendarDate(
    val year: Int,
    val month: Int, // 1 ~ 12
    val day: Int,
)

private val WEEKDAY_LABELS = listOf("일", "월", "화", "수", "목", "금", "토")

@Composable
fun DateMultiSelectCalendar(
    selected: Set<CalendarDate>,
    onSelectedChange: (Set<CalendarDate>) -> Unit,
    modifier: Modifier = Modifier,
    initialYear: Int = currentYear(),
    initialMonth: Int = currentMonth(),
) {
    var year by remember { mutableIntStateOf(initialYear) }
    var month by remember { mutableIntStateOf(initialMonth) }

    Column(modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
        CalendarHeader(
            year = year,
            month = month,
            onPrevious = {
                if (month == 1) {
                    year -= 1
                    month = 12
                } else {
                    month -= 1
                }
            },
            onNext = {
                if (month == 12) {
                    year += 1
                    month = 1
                } else {
                    month += 1
                }
            },
        )
        HorizontalDivider(modifier = Modifier.padding(top = 10.dp).height(1.dp).background(color = LIGHT_GRAY).border(width = 1.dp, color = LIGHT_GRAY))
        WeekdayHeader()
        CalendarGrid(
            year = year,
            month = month,
            selected = selected,
            onDateClick = { date ->
                onSelectedChange(
                    if (date in selected) selected - date else selected + date,
                )
            },
        )
    }
}

@Composable
private fun CalendarHeader(
    year: Int,
    month: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
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
            color = BLACK,
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
            color = BLACK,
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
            .padding(start = 13.dp, end = 13.dp, top = 16.dp, bottom = 12.dp),
    ) {
        WEEKDAY_LABELS.forEach { label ->
            Box(
                modifier = Modifier
                    .weight(1f),
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
    selected: Set<CalendarDate>,
    onDateClick: (CalendarDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    // 해당 월의 첫 요일 오프셋(일=0)과 총 일수 계산
    val calendar = remember(year, month) {
        Calendar.getInstance().apply {
            clear()
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, 1)
        }
    }
    val firstDayOffset = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY // 그리드에서 앞쪽에 비워둘 셀 개수
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) // 해당 월 총 일수
    val totalCells = firstDayOffset + daysInMonth
    val rowCount = (totalCells + 6) / 7

    Column(
        modifier = modifier
            .fillMaxWidth().padding(horizontal = 13.dp),
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
                            val date = CalendarDate(year, month, day)
                            DateCell(
                                day = day,
                                isSelected = date in selected,
                                onClick = { onDateClick(date) },
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
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cellModifier = if (isSelected) {
        Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(Green_D6F695, Green_F1FFD4),
                ),
            )
    } else {
        Modifier.size(28.dp)
    }

    Box(
        modifier = modifier
            .then(cellModifier)
            .noRippleClickable(onClick),
        contentAlignment = Alignment.Center,
    ) {
        YogheeText(
            text = day.toString(),
            color = BLACK,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
        )
    }
}

private fun currentYear(): Int = Calendar.getInstance().get(Calendar.YEAR)

private fun currentMonth(): Int = Calendar.getInstance().get(Calendar.MONTH) + 1

@Preview(showBackground = true, name = "DateMultiSelectCalendar")
@Composable
private fun DateMultiSelectCalendarPreview() {
    var selected by remember {
        mutableStateOf(
            setOf(
                CalendarDate(2026, 7, 5),
                CalendarDate(2026, 7, 12),
                CalendarDate(2026, 7, 20),
            ),
        )
    }
    YogheeTheme {
        DateMultiSelectCalendar(
            selected = selected,
            onSelectedChange = { selected = it },
            initialYear = 2026,
            initialMonth = 7,
            modifier = Modifier.padding(vertical = 12.dp),
        )
    }
}
