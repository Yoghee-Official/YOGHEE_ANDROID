package com.teamyoga.yoghee.core.ui.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.theme.GRAY
import com.teamyoga.yoghee.core.ui.theme.MIND_ORANGE
import com.teamyoga.yoghee.core.ui.util.noRippleClickable

private val TrackWidth = 46.dp
private val TrackHeight = 31.dp
private val ThumbSize = 21.dp
private val ThumbPadding = 5.dp
private val ThumbOffsetOn = TrackWidth - ThumbSize - ThumbPadding // 20.dp
private val ThumbOffsetOff = ThumbPadding // 5.dp

@Composable
fun YogheeToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val thumbOffsetX by animateDpAsState(
        targetValue = if (checked) ThumbOffsetOn else ThumbOffsetOff,
        animationSpec = tween(durationMillis = 200),
        label = "thumbOffset",
    )

    Box(
        modifier = modifier
            .size(width = TrackWidth, height = TrackHeight)
            .paint(
                painter = painterResource(R.drawable.bg_toggle),
                contentScale = ContentScale.FillBounds,
            )
            .noRippleClickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffsetX)
                .size(ThumbSize)
                .clip(CircleShape)
                .background(if (checked) MIND_ORANGE else GRAY),
        )
    }
}

@Preview(showBackground = true, name = "YogheeToggle Off")
@Composable
private fun YogheeToggleOffPreview() {
    YogheeToggle(checked = false, onCheckedChange = {})
}

@Preview(showBackground = true, name = "YogheeToggle On")
@Composable
private fun YogheeToggleOnPreview() {
    YogheeToggle(checked = true, onCheckedChange = {})
}

@Preview(showBackground = true, name = "YogheeToggle Interactive")
@Composable
private fun YogheeToggleInteractivePreview() {
    var checked by remember { mutableStateOf(false) }
    YogheeToggle(checked = checked, onCheckedChange = { checked = it })
}
