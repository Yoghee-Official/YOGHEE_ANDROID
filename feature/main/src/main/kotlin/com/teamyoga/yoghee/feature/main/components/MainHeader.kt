package com.teamyoga.yoghee.feature.main.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.draw.shadow
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.util.noRippleClickable
import com.teamyoga.yoghee.feature.main.TrainingType

@Composable
fun MainHeader(
    trainingType: TrainingType,
    onTrainingTypeChanged: (TrainingType) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .statusBarsPadding(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.header_logo),
                contentDescription = "로고",
                modifier = Modifier
                    .padding(start = 25.dp, top = 28.dp, bottom = 9.dp)
                    .size(width = 107.dp, height = 24.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CategoryToggle(
                    trainingType = trainingType,
                    onTrainingTypeChanged = onTrainingTypeChanged
                )
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(id = R.drawable.ic_bird),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 4.dp)
                        .size(width = 30.dp, height = 24.dp)
                )
            }
        }
    }
}

@Composable
fun CategoryToggle(
    trainingType: TrainingType,
    onTrainingTypeChanged: (TrainingType) -> Unit,
    modifier: Modifier = Modifier
) {
    val pillOffsetX by animateDpAsState(
        targetValue = if (trainingType == TrainingType.REGULAR) 68.dp else 0.dp,
        animationSpec = tween(durationMillis = 200),
        label = "toggleIndicator"
    )

    Box(
        modifier = modifier
            .width(144.dp)
            .height(32.dp)
            .clip(RoundedCornerShape(29.dp))
            .dropShadow(
                shape = RoundedCornerShape(29.dp),
                shadow = Shadow(
                    radius = 4.dp,
                    spread = 1.dp,
                    color = Color.Black.copy(alpha = 0.15f),
                    offset = DpOffset(x = 0.dp, y = 0.dp)
                )
            )
            .background(Color.White)
    ) {
        // 슬라이딩 인디케이터
        Box(
            modifier = Modifier
                .offset(x = pillOffsetX)
                .width(76.dp)
                .fillMaxHeight()
                .shadow(1.dp, RoundedCornerShape(29.dp), ambientColor = Color.Black.copy(alpha = 0.1f))
                .clip(RoundedCornerShape(29.dp))
                .background(Color(0xFFD6F695))
        )

        // 토글 텍스트
        Row(modifier = Modifier.fillMaxSize()) {
            TrainingType.entries.forEach { type ->
                val isSelected = trainingType == type
                val labelRes = when (type) {
                    TrainingType.DAILY -> R.string.daily_training
                    TrainingType.REGULAR -> R.string.regular_training
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .noRippleClickable { onTrainingTypeChanged(type) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(labelRes),
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = Color.Black.copy(alpha = if (isSelected) 1f else 0.2f)
                    )
                }
            }
        }
    }
}
