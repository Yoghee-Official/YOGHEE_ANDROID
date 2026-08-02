package com.teamyoga.yoghee.core.ui.component

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.GRAY
import com.teamyoga.yoghee.core.ui.theme.LIGHT_GRAY

/**
 * 세로 스크롤 형태의 휠 선택 UI.
 * 중앙에 위치한 아이템이 현재 선택된 값으로 간주된다.
 *
 * @param items 표시할 항목 리스트
 * @param initialIndex 최초 선택 index
 * @param onSelectedIndexChange 중앙 아이템(선택 값)이 바뀔 때 호출
 * @param itemHeight 각 아이템 높이 (선택 영역 높이와 동일)
 * @param visibleItemCount 화면에 보일 아이템 개수 (홀수 권장)
 * @param itemContent 아이템 렌더링 slot. isSelected로 중앙 여부 전달됨
 */
@Composable
fun <T> WheelPicker(
    items: List<T>,
    initialIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    itemHeight: Dp = 40.dp,
    visibleItemCount: Int = 5,
    itemContent: @Composable (item: T, isSelected: Boolean) -> Unit = { item, isSelected ->
        DefaultWheelItem(text = item.toString(), isSelected = isSelected)
    },
) {
    // 표시 개수는 홀수여야 중앙 정렬 가능
    val safeVisibleCount = if (visibleItemCount % 2 == 0) visibleItemCount + 1 else visibleItemCount
    val paddingCount = safeVisibleCount / 2

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    // 첫 번째로 보이는 index가 곧 중앙 아이템의 실제 index (앞에 padding 2개를 넣었기 때문)
    val selectedIndex by remember {
        derivedStateOf {
            val raw = listState.firstVisibleItemIndex
            raw.coerceIn(0, items.lastIndex)
        }
    }

    LaunchedEffect(selectedIndex) {
        onSelectedIndexChange(selectedIndex)
    }

    Box(
        modifier = modifier.height(itemHeight * safeVisibleCount),
        contentAlignment = Alignment.Center,
    ) {
        // 중앙 선택 영역 강조용 상하 구분선
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight),
        ) {
            HorizontalDivider(
                modifier = Modifier.align(Alignment.TopCenter),
                thickness = 1.dp,
                color = LIGHT_GRAY,
            )
            HorizontalDivider(
                modifier = Modifier.align(Alignment.BottomCenter),
                thickness = 1.dp,
                color = LIGHT_GRAY,
            )
        }

        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxWidth(),
        ) {
            // 앞뒤 padding 아이템: 첫/마지막 실제 아이템도 중앙에 올 수 있도록
            itemsIndexed(List(paddingCount) { it }) { _, _ ->
                Spacer(modifier = Modifier.height(itemHeight))
            }
            itemsIndexed(items) { index, item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight),
                    contentAlignment = Alignment.Center,
                ) {
                    itemContent(item, index == selectedIndex)
                }
            }
            itemsIndexed(List(paddingCount) { it }) { _, _ ->
                Spacer(modifier = Modifier.height(itemHeight))
            }
        }
    }
}

@Composable
private fun DefaultWheelItem(text: String, isSelected: Boolean) {
    YogheeText(
        text = text,
        color = if (isSelected) BLACK else GRAY,
        fontSize = if (isSelected) 20.sp else 16.sp,
        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
    )
}

@Preview(showBackground = true, name = "WheelPicker - Hours")
@Composable
private fun WheelPickerPreview() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        WheelPicker(
            items = (0..23).toList(),
            initialIndex = 9,
            onSelectedIndexChange = {},
            modifier = Modifier.padding(horizontal = 24.dp),
            itemContent = { item, isSelected ->
                DefaultWheelItem(text = "%02d".format(item), isSelected = isSelected)
            },
        )
        WheelPicker(
            items = (0..59).toList(),
            initialIndex = 30,
            onSelectedIndexChange = {},
            modifier = Modifier.padding(horizontal = 24.dp),
            itemContent = { item, isSelected ->
                DefaultWheelItem(text = "%02d".format(item), isSelected = isSelected)
            },
        )
    }
}
