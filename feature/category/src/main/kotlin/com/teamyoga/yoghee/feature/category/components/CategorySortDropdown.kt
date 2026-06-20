package com.teamyoga.yoghee.feature.category.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.GRAY_D9D9D9
import com.teamyoga.yoghee.core.ui.theme.SAND_BEIGE
import com.teamyoga.yoghee.core.ui.theme.WHITE
import com.teamyoga.yoghee.core.ui.util.noRippleClickable
import com.teamyoga.yoghee.feature.category.CategorySort

@Composable
fun CategorySortDropdown(
    selected: CategorySort,
    onSortSelected: (CategorySort) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    var triggerHeightPx by remember { mutableIntStateOf(0) }
    val triggerHeightDp = with(LocalDensity.current) { triggerHeightPx.toDp() }

    val sortedOptions = remember(selected) {
        listOf(selected) + CategorySort.entries.filter { it != selected }
    }

    Box(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .noRippleClickable { expanded = true }
                .onSizeChanged { triggerHeightPx = it.height }
                .padding(start = 28.dp, top = 16.dp, bottom = 8.dp),
        ) {
            Text(
                text = selected.label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = BLACK,
            )
            Image(
                painter = painterResource(R.drawable.ic_arrow_dropdown),
                contentDescription = null,
                modifier = Modifier.padding(start = 4.dp).width(8.dp).height(4.dp),
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(x = 16.dp, y = -triggerHeightDp),
            shape = RoundedCornerShape(8.dp),
            containerColor = SAND_BEIGE,
        ) {
            sortedOptions.forEachIndexed { index, sort ->
                val isSelected = sort == selected
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = sort.label,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = BLACK,
                            )
                            if (isSelected) {
                                Image(
                                    painter = painterResource(R.drawable.ic_arrow_dropdown),
                                    contentDescription = null,
                                    modifier = Modifier.padding(start = 4.dp).width(8.dp).height(4.dp),
                                )
                            }
                        }
                    },
                    onClick = {
                        expanded = false
                        onSortSelected(sort)
                    },
                    modifier = Modifier.height(32.dp),
                )
                if (index == 0) {
                    HorizontalDivider(thickness = 1.dp, color = BLACK, modifier = Modifier.padding(horizontal = 12.dp))
                }
            }
        }
    }
}
