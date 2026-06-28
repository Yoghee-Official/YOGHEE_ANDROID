package com.teamyoga.yoghee.feature.category.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.Green_D6F695
import com.teamyoga.yoghee.core.ui.theme.LIGHT_GRAY
import com.teamyoga.yoghee.core.ui.util.noRippleClickable
import com.teamyoga.yoghee.feature.category.CategoryTab

@Composable
fun CategoryTabChips(
    tabs: List<CategoryTab>,
    selectedTabId: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
    ) {
        items(tabs, key = { it.id }) { tab ->
            CategoryChip(
                name = tab.name,
                selected = tab.id == selectedTabId,
                onClick = { onTabSelected(tab.id) },
            )
        }
    }
}

@Composable
private fun CategoryChip(
    name: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val bgColor = if (selected) Green_D6F695 else LIGHT_GRAY

    Text(
        text = name,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = BLACK,
        modifier = Modifier
            .clip(RoundedCornerShape(32.dp))
            .background(bgColor)
            .noRippleClickable(onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
    )
}