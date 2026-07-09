package com.teamyoga.yoghee.feature.registerClass.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.Green_D6F695
import com.teamyoga.yoghee.core.ui.theme.LIGHT_GRAY
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme
import com.teamyoga.yoghee.core.ui.util.noRippleClickable

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MultiSelectChipsSection(
    title: String,
    subTitle: String,
    options: List<String>,
    selected: Set<String>,
    onSelectedChange: (Set<String>) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 10.dp),
    ) {
        RegisterSectionTitle(
            title = title,
            subTitle = subTitle,
            modifier = Modifier.padding(bottom = 12.dp),
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            options.forEach { option ->
                val isSelected = option in selected
                SelectableChip(
                    text = option,
                    isSelected = isSelected,
                    onClick = {
                        onSelectedChange(
                            if (isSelected) selected - option else selected + option,
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun SelectableChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (isSelected) Green_D6F695 else LIGHT_GRAY

    YogheeText(
        text = "+ $text",
        color = BLACK,
        fontSize = 12.sp,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
        modifier = modifier
            .clip(RoundedCornerShape(32.dp))
            .background(backgroundColor)
            .noRippleClickable(onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
    )
}

@Preview(showBackground = true, name = "MultiSelectChipsSection")
@Composable
private fun MultiSelectChipsSectionPreview() {
    val options = listOf(
        "아쉬탕가", "아헹가", "하타", "빈야사", "인요가",
        "테라피", "명상", "쉬바난다", "비프로스플로우",
        "인사이드플로우", "플라잉요가", "소도구요가", "파트너요가",
        "임산부 요가", "펫요가", "키즈요가", "기타",
    )
    var selected by remember { mutableStateOf(setOf("하타", "빈야사")) }
    YogheeTheme {
        MultiSelectChipsSection(
            title = "전문 수련 유형",
            subTitle = "상세페이지에 노출되는 수련 유형이예요.",
            options = options,
            selected = selected,
            onSelectedChange = { selected = it },
            modifier = Modifier.padding(vertical = 24.dp),
        )
    }
}
