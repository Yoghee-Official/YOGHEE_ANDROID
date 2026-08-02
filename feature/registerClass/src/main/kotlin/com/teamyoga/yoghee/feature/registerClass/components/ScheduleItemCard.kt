package com.teamyoga.yoghee.feature.registerClass.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.GRAY
import com.teamyoga.yoghee.core.ui.theme.LIGHT_GRAY
import com.teamyoga.yoghee.core.ui.theme.WHITE
import com.teamyoga.yoghee.core.ui.util.noRippleClickable

@Composable
fun ScheduleItemCard(
    schedule: ClassSchedule,
    modifier: Modifier = Modifier,
    onEdit: () -> Unit = {},
    onCopy: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(WHITE)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            YogheeText(
                text = schedule.className.ifBlank { "(수련명 없음)" },
                color = BLACK,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
            )
            ScheduleMoreMenu(
                onEdit = onEdit,
                onCopy = onCopy,
                onDelete = onDelete,
            )
        }
        YogheeText(
            text = formatDates(schedule.dates),
            color = BLACK,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        IconTextRow(
            iconResId = R.drawable.ic_clock,
            text = "${schedule.startTime} - ${schedule.endTime}",
        )
        IconTextRow(
            iconResId = R.drawable.ic_people,
            text = "최소 수강 인원 ${schedule.minCount}명 ~ 최대 ${schedule.maxCount}명",
        )
    }
}

@Composable
private fun ScheduleMoreMenu(
    onEdit: () -> Unit,
    onCopy: () -> Unit,
    onDelete: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val gapPx = with(density) { 8.dp.roundToPx() }

    Box {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "메뉴 열기",
            tint = GRAY,
            modifier = Modifier
                .size(20.dp)
                .noRippleClickable { expanded = true },
        )
        if (expanded) {
            Popup(
                onDismissRequest = { expanded = false },
                properties = PopupProperties(focusable = true),
                popupPositionProvider = remember(gapPx) {
                    // 앵커(점세개) 왼쪽 8dp 떨어진 지점에 메뉴 우측 끝, top은 앵커 top과 동일
                    object : PopupPositionProvider {
                        override fun calculatePosition(
                            anchorBounds: IntRect,
                            windowSize: IntSize,
                            layoutDirection: LayoutDirection,
                            popupContentSize: IntSize,
                        ): IntOffset = IntOffset(
                            x = anchorBounds.left - popupContentSize.width - gapPx,
                            y = anchorBounds.top,
                        )
                    }
                },
            ) {
                MoreMenuContent(
                    onEdit = {
                        expanded = false
                        onEdit()
                    },
                    onCopy = {
                        expanded = false
                        onCopy()
                    },
                    onDelete = {
                        expanded = false
                        onDelete()
                    },
                )
            }
        }
    }
}

@Composable
private fun MoreMenuContent(
    onEdit: () -> Unit,
    onCopy: () -> Unit,
    onDelete: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = WHITE,
        border = BorderStroke(1.dp, LIGHT_GRAY),
        shadowElevation = 0.dp,
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MoreMenuItem(text = "수정", onClick = onEdit)
            HorizontalDivider(
                modifier = Modifier.width(40.dp).padding(vertical = 4.dp),
                thickness = 1.dp,
                color = LIGHT_GRAY
            )
            MoreMenuItem(text = "복사", onClick = onCopy)
            HorizontalDivider(
                modifier = Modifier.width(40.dp).padding(vertical = 4.dp),
                thickness = 1.dp,
                color = LIGHT_GRAY
            )
            MoreMenuItem(text = "삭제", onClick = onDelete)
        }
    }
}

@Composable
private fun MoreMenuItem(
    text: String,
    onClick: () -> Unit,
) {
    YogheeText(
        text = text,
        color = BLACK,
        fontSize = 14.sp,
        modifier = Modifier.noRippleClickable(onClick),
    )
}

@Composable
private fun IconTextRow(
    iconResId: Int,
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Image(
            painter = painterResource(iconResId),
            contentDescription = null,
            modifier = Modifier.size(12.dp),
        )
        YogheeText(
            text = text,
            color = BLACK,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
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
            val days = list.joinToString(",") { "${it.day}일" }
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
