package com.teamyoga.yoghee.feature.detail.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.domain.model.ClassSchedule
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.GHEE_YELLO
import com.teamyoga.yoghee.core.ui.theme.GRAY
import com.teamyoga.yoghee.core.ui.theme.LIGHT_GRAY
import com.teamyoga.yoghee.core.ui.theme.WHITE
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme
import java.util.Calendar

private const val MONTHS_RANGE = 12
private val WEEKDAY_SHORT = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

/**
 * 수련 시간표 섹션.
 * - 상단: 구분선 + 라운드 타이틀
 * - 중단: 수업 있는 날짜만 선택 가능한 캘린더 (±1년)
 * - 하단: 선택 날짜에 속한 수업 리스트
 */
@Composable
fun DetailTimeTable(
    title: String,
    schedules: List<ClassSchedule>,
    modifier: Modifier = Modifier,
) {
    if (schedules.isEmpty()) return

    // specificDate(yyyy-MM-dd) 를 파싱해 날짜 → 수업 리스트로 그룹핑. 포맷 어긋나면 무시.
    val schedulesByDate = remember(schedules) {
        schedules
            .mapNotNull { schedule -> parseScheduleDate(schedule.specificDate)?.let { it to schedule } }
            .groupBy({ it.first }, { it.second })
    }
    if (schedulesByDate.isEmpty()) return

    val scheduleDates = schedulesByDate.keys
    val today = remember { todayScheduleDate() }
    var selected by remember { mutableStateOf<ScheduleDate?>(null) }

    val (minYear, minMonth) = remember(today) { addMonths(today.year, today.month, -MONTHS_RANGE) }
    val (maxYear, maxMonth) = remember(today) { addMonths(today.year, today.month, MONTHS_RANGE) }

    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalDivider(
            thickness = 0.5.dp,
            color = GRAY,
            modifier = Modifier.padding(top = 32.dp, bottom = 32.dp, start = 16.dp, end = 16.dp),
        )
        RoundBorderTitle(text = title, modifier = Modifier.padding(start = 16.dp))
        Spacer(modifier = Modifier.height(16.dp))
        DateScheduleCalendar(
            scheduleDates = scheduleDates,
            selected = selected,
            onDateClick = { selected = it },
            minYear = minYear,
            minMonth = minMonth,
            maxYear = maxYear,
            maxMonth = maxMonth,
            initialYear = today.year,
            initialMonth = today.month,
        )
        val selectedSchedules = selected?.let { schedulesByDate[it] }.orEmpty()
        if (selectedSchedules.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(color = GHEE_YELLO, shape = RoundedCornerShape(8.dp))
                    .padding(vertical = 10.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                selectedSchedules.forEach { schedule ->
                    ScheduleRow(date = selected, schedule = schedule)
                }
            }
        }
    }
}

@Composable
fun RegularScheduleTable(
    title: String,
    schedules: List<ClassSchedule>,
    modifier: Modifier = Modifier,
) {
    if (schedules.isEmpty()) return

    val weekdayNames = listOf("월", "화", "수", "목", "금", "토", "일")
    val today = remember { todayScheduleDate() }
    var monthOffset by remember { mutableIntStateOf(0) }
    val (displayYear, displayMonth) = remember(today, monthOffset) {
        addMonths(today.year, today.month, monthOffset)
    }
    val gridStartMinute = (schedules.minOf { it.startTime.toMinuteOfDay() } / 60 * 60).coerceAtMost(8 * 60)
    val gridEndMinute = (((schedules.maxOf { it.endTime.toMinuteOfDay() } + 59) / 60) * 60)
        .coerceAtLeast(21 * 60)
        .coerceAtMost(24 * 60)
    val pixelsPerMinute = 1.1f
    val gridHeight = ((gridEndMinute - gridStartMinute) * pixelsPerMinute).dp
    val scrollState = rememberScrollState()

    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalDivider(
            thickness = 0.5.dp,
            color = GRAY,
            modifier = Modifier.padding(top = 32.dp, bottom = 32.dp, start = 16.dp, end = 16.dp),
        )

        RoundBorderTitle(text = title, modifier = Modifier.padding(bottom = 16.dp, start = 16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(
                    color = LIGHT_GRAY,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                )
                .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            YogheeText(
                text = displayYear.toString(),
                color = BLACK,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "◀",
                color = GRAY,
                fontSize = 8.sp,
                modifier = Modifier.clickable { monthOffset-- },
            )
            YogheeText(
                text = "${displayMonth}월",
                color = BLACK,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 30.dp)
            )
            Text(
                text = "▶",
                color = BLACK,
                fontSize = 8.sp,
                modifier = Modifier.clickable { monthOffset++ },
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(
                    color = LIGHT_GRAY,
                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                )
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            weekdayNames.forEach { weekday ->
                YogheeText(
                    text = weekday,
                    color = BLACK,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(540.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFFAF9F6))
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp),
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxWidth().height(gridHeight)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val hourSpacing = (60 * pixelsPerMinute).dp.toPx()
                    var y = hourSpacing
                    while (y < size.height) {
                        drawLine(
                            color = Color(0xFFE7E5E1),
                            start = androidx.compose.ui.geometry.Offset(0f, y),
                            end = androidx.compose.ui.geometry.Offset(size.width, y),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                                floatArrayOf(5.dp.toPx(), 4.dp.toPx()),
                            ),
                        )
                        y += hourSpacing
                    }
                }
                Row(modifier = Modifier.fillMaxSize()) {
                    weekdayNames.indices.forEach { index ->
                        Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                            if (index > 0) {
                                androidx.compose.foundation.Canvas(
                                    modifier = Modifier.fillMaxHeight().width(1.dp),
                                ) {
                                    drawLine(
                                        color = Color(0xFFE7E5E1),
                                        start = androidx.compose.ui.geometry.Offset(size.width / 2, 0f),
                                        end = androidx.compose.ui.geometry.Offset(size.width / 2, size.height),
                                        strokeWidth = 1.dp.toPx(),
                                        pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                                            floatArrayOf(5.dp.toPx(), 4.dp.toPx()),
                                        ),
                                    )
                                }
                            }
                            schedules.filter { it.dayOfWeek == index + 1 }
                                .sortedBy { it.startTime }
                                .forEachIndexed { cardIndex, schedule ->
                                    val cardStart = schedule.startTime.toMinuteOfDay()
                                    val cardEnd = schedule.endTime.toMinuteOfDay()
                                    val top = ((cardStart - gridStartMinute).coerceAtLeast(0) * pixelsPerMinute).dp
                                    val cardHeight = ((cardEnd - cardStart).coerceAtLeast(35) * pixelsPerMinute).dp
                                    val cardColor = if (cardIndex % 3 == 1) Color(0xFFD7F59A) else Color(0xFFCCE1FA)
                                    Column(
                                        modifier = Modifier
                                            .offset(y = top)
                                            .padding(horizontal = 2.dp)
                                            .fillMaxWidth()
                                            .height(cardHeight)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(cardColor)
                                            .padding(horizontal = 2.dp, vertical = 6.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                    ) {
                                        YogheeText(
                                            text = "${schedule.startTime}\n~ ${schedule.endTime}",
                                            color = BLACK,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center,
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        YogheeText(
                                            text = schedule.content,
                                            color = BLACK,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center,
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        YogheeText(
                                            text = schedule.instructorName,
                                            color = BLACK,
                                            fontSize = 9.sp,
                                            textAlign = TextAlign.Center,
                                        )
                                    }
                                }
                        }
                    }
                }
            }
        }
    }
}

private fun String.toMinuteOfDay(): Int {
    val parts = split(":")
    val hour = parts.getOrNull(0)?.toIntOrNull() ?: return 0
    val minute = parts.getOrNull(1)?.toIntOrNull() ?: return 0
    return hour.coerceIn(0, 23) * 60 + minute.coerceIn(0, 59)
}

@Composable
private fun ScheduleRow(
    date: ScheduleDate?,
    schedule: ClassSchedule,
    modifier: Modifier = Modifier,
) {
    if (date == null) {
        return
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .width(44.dp)
                .background(color = WHITE, shape = RoundedCornerShape(100.dp))
                .padding(vertical = 9.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            YogheeText(
                text = "%02d".format(date.day),
                color = BLACK,
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
            )
            YogheeText(
                text = weekdayLabel(date),
                color = BLACK,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(modifier = Modifier.width(13.dp))
        Column(modifier = Modifier.weight(1f)) {
            YogheeText(
                text = schedule.content,
                color = BLACK,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(5.dp))
            YogheeText(
                text = "${formatMeridiemTime(schedule.startTime)} ~ ${formatMeridiemTime(schedule.endTime)} | 예약 가능 인원 : ${schedule.remainingCapacity}명",
                color = BLACK,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

/**
 * ScheduleDate 로 변환.
 * - "yyyy-MM-dd" / "yyyy-MM-ddTHH:mm:ss.SSSZ" 둘 다 허용 (ISO 접미사는 'T' 앞까지 잘라 사용).
 * - 포맷 어긋나면 null.
 */
internal fun parseScheduleDate(raw: String): ScheduleDate? {
    val dateOnly = raw.substringBefore('T')
    val parts = dateOnly.split("-")
    if (parts.size != 3) return null
    val year = parts[0].toIntOrNull() ?: return null
    val month = parts[1].toIntOrNull() ?: return null
    val day = parts[2].toIntOrNull() ?: return null
    if (month !in 1..12 || day !in 1..31) return null
    return ScheduleDate(year, month, day)
}

internal fun addMonths(year: Int, month: Int, delta: Int): Pair<Int, Int> {
    val totalMonths = (year * 12 + (month - 1)) + delta
    val newYear = totalMonths / 12
    val newMonth = (totalMonths % 12) + 1
    return newYear to newMonth
}

internal fun todayScheduleDate(): ScheduleDate {
    val cal = Calendar.getInstance()
    return ScheduleDate(
        year = cal.get(Calendar.YEAR),
        month = cal.get(Calendar.MONTH) + 1,
        day = cal.get(Calendar.DAY_OF_MONTH),
    )
}

/**
 * "HH:mm" 을 "오전/오후 h:mm" 로 포맷. 포맷 어긋나면 원본 반환.
 * - 0시 → "오전 12:00"
 * - 12시 → "오후 12:00"
 */
private fun formatMeridiemTime(raw: String): String {
    val parts = raw.split(":")
    if (parts.size < 2) return raw
    val hour = parts[0].toIntOrNull() ?: return raw
    val minute = parts[1].toIntOrNull() ?: return raw
    if (hour !in 0..23 || minute !in 0..59) return raw
    val meridiem = if (hour < 12) "오전" else "오후"
    val hour12 = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }
    return "%s %d:%02d".format(meridiem, hour12, minute)
}

private fun weekdayLabel(date: ScheduleDate): String {
    val cal = Calendar.getInstance().apply {
        clear()
        set(Calendar.YEAR, date.year)
        set(Calendar.MONTH, date.month - 1)
        set(Calendar.DAY_OF_MONTH, date.day)
    }
    val dow = cal.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY
    return WEEKDAY_SHORT[dow]
}

@Preview(showBackground = true, name = "DetailTimeTable")
@Composable
private fun DetailTimeTablePreview() {
    val sample = listOf(
        sampleSchedule("2026-10-13", "하타 요가", "10:00", "11:00", 5),
        sampleSchedule("2026-10-13", "빈야사", "19:00", "20:00", 2),
        sampleSchedule("2026-10-20", "플라잉 요가", "20:00", "21:00", 8),
    )
    YogheeTheme {
        DetailTimeTable(title = "수련 시간표", schedules = sample)
    }
}

private fun sampleSchedule(
    date: String,
    content: String,
    startTime: String,
    endTime: String,
    remaining: Int,
): ClassSchedule = ClassSchedule(
    scheduleId = "s-$date-$startTime",
    sessionId = "sess",
    sessionStatus = "ACTIVE",
    currentCount = 0,
    remainingCapacity = remaining,
    bookable = true,
    unavailableReason = "",
    alreadyReserved = false,
    dayOfWeek = 0,
    specificDate = date,
    startTime = startTime,
    endTime = endTime,
    minCapacity = 1,
    maxCapacity = 10,
    content = content,
    instructorName = "김요가",
    effectiveFrom = "",
    effectiveTo = "",
)
