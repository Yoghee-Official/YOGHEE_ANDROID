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
    options: List<Pair<String, String>>,
    selected: Set<String>,
    onSelectedChange: (Set<String>) -> Unit,
    modifier: Modifier = Modifier,
    titleModifier: Modifier = Modifier.padding(bottom = 12.dp, start = 8.dp),
    itemModifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
    ) {
        RegisterSectionTitle(
            title = title,
            subTitle = subTitle,
            modifier = titleModifier,
        )
        FlowRow(
            modifier = itemModifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            options.forEach { (code, label) ->
                val isSelected = code in selected
                SelectableChip(
                    text = label,
                    isSelected = isSelected,
                    onClick = {
                        onSelectedChange(
                            if (isSelected) selected - code else selected + code,
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
        "ashtanga" to "아쉬탕가",
        "iyengar" to "아헹가",
        "hatha" to "하타",
        "vinyasa" to "빈야사",
        "yin_yoga" to "인요가",
    )
    var selected by remember { mutableStateOf(setOf("hatha", "vinyasa")) }
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
