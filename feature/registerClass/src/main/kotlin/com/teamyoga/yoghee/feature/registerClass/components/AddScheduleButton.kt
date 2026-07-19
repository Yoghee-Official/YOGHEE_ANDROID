package com.teamyoga.yoghee.feature.registerClass.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.GHEE_YELLO
import com.teamyoga.yoghee.core.ui.util.noRippleClickable

@Composable
fun AddScheduleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(34.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(GHEE_YELLO)
            .noRippleClickable(onClick),
        contentAlignment = Alignment.Center,
    ) {
        YogheeText(
            text = "+ 선택한 날짜에 수련 추가",
            color = BLACK,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}
