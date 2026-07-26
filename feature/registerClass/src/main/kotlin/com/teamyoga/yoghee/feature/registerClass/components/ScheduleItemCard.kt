package com.teamyoga.yoghee.feature.registerClass.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.GRAY
import com.teamyoga.yoghee.core.ui.theme.WHITE

@Composable
fun ScheduleItemCard(
    schedule: ClassSchedule,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(WHITE)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        YogheeText(
            text = schedule.className.ifBlank { "(수련명 없음)" },
            color = BLACK,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
        )
        YogheeText(
            text = formatDates(schedule.dates),
            color = BLACK,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        YogheeText(
            text = "${schedule.startTime} - ${schedule.endTime}",
            color = BLACK,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth()
        )
        YogheeText(
            text = "최소 수강 인원 ${schedule.minCount}명 ~ 최대 ${schedule.maxCount}명",
            color = BLACK,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun formatDates(dates: Set<CalendarDate>): String {
    if (dates.isEmpty()) return "-"
    return dates
        .sortedWith(compareBy({ it.year }, { it.month }, { it.day }))
        .groupBy { it.year to it.month }
        .map { (yearMonth, list) ->
            val (year, month) = yearMonth
            val days = list.joinToString(", ") { "${it.day}일" }
            "${year % 100}년 ${month}월 $days"
        }
        .joinToString(" / ")
}

@Preview(showBackground = true, name = "ScheduleItemCard")
@Composable
private fun ScheduleItemCardPreview() {
    ScheduleItemCard(
        schedule = ClassSchedule(
            startTime = "09:00",
            endTime = "10:30",
            className = "아침 아쉬탕가",
            minCount = 3,
            maxCount = 15,
            dates = setOf(
                CalendarDate(2026, 12, 30),
                CalendarDate(2026, 12, 31),
                CalendarDate(2027, 1, 2),
            ),
        ),
        modifier = Modifier.padding(16.dp),
    )
}
