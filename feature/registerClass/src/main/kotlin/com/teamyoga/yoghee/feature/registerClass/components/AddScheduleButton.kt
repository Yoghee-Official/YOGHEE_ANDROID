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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.GHEE_YELLO
import com.teamyoga.yoghee.core.ui.theme.LIGHT_GRAY
import com.teamyoga.yoghee.core.ui.util.noRippleClickable

@Composable
fun AddScheduleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(34.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if(enabled) GHEE_YELLO else LIGHT_GRAY)
            .then(if (enabled) Modifier.noRippleClickable(onClick) else Modifier),
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

@Preview(showBackground = true, name = "AddScheduleButton - Enabled")
@Composable
private fun AddScheduleButtonEnabledPreview() {
    AddScheduleButton(onClick = {}, enabled = true)
}

@Preview(showBackground = true, name = "AddScheduleButton - Disabled")
@Composable
private fun AddScheduleButtonDisabledPreview() {
    AddScheduleButton(onClick = {}, enabled = false)
}
