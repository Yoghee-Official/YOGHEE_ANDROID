package com.teamyoga.yoghee.feature.registerClass.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.LIGHT_GRAY
import com.teamyoga.yoghee.core.ui.theme.WHITE
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme
import com.teamyoga.yoghee.core.ui.util.noRippleClickable

private const val MAX_SELECTION_COUNT = 3

private val PURPOSE_OPTIONS = listOf(
    "기본 수련 경험이 있고 흐름 있는 동작",
    "허리·골반 주변 이완 및 안정",
    "요가 입문자, 기본 동작과 호흡 설명 중심",
    "몸이 뻣뻣하거나 스트레칭 위주 수련",
    "중심 잡기, 안정성, 자세 정렬에 집중하는 수련",
    "호흡·이완 중심, 심리적 안정",
)

@Composable
fun ClassPurposeSection(
    modifier: Modifier = Modifier,
) {
    var selected by remember { mutableStateOf<Set<String>>(emptySet()) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 10.dp),
    ) {
        RegisterSectionTitle(
            title = "어디에 도움되는 수업인가요?",
            subTitle = "최대 3개까지 자유롭게 선택 가능합니다.",
            modifier = Modifier.padding(bottom = 2.dp)
        )

        PURPOSE_OPTIONS.forEach { option ->
            val isSelected = option in selected
            PurposeCheckItem(
                text = option,
                isSelected = isSelected,
                onClick = {
                    selected = when {
                        isSelected -> selected - option
                        // 최대 개수 도달 시 새 선택 무시
                        selected.size >= MAX_SELECTION_COUNT -> selected
                        else -> selected + option
                    }
                },
            )
        }
    }
}

@Composable
private fun PurposeCheckItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(top = 8.dp)
            .clip(RoundedCornerShape(23.dp))
            .background(WHITE)
            .border(width = 1.dp, color = LIGHT_GRAY, shape = RoundedCornerShape(23.dp))
            .noRippleClickable(onClick)
            .padding(start = 8.dp, end = 12.dp, top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Image(
            painter = if (isSelected) painterResource(R.drawable.ic_checked) else painterResource(R.drawable.ic_unchecked),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        YogheeText(
            text = text,
            color = BLACK,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Preview(showBackground = true, name = "ClassPurposeSection")
@Composable
private fun ClassPurposeSectionPreview() {
    YogheeTheme {
        ClassPurposeSection(
            modifier = Modifier.padding(24.dp),
        )
    }
}
