package com.teamyoga.yoghee.feature.registerClass.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.component.YogheeImage
import com.teamyoga.yoghee.core.ui.theme.LIGHT_GRAY
import com.teamyoga.yoghee.core.ui.util.noRippleClickable
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState

@Composable
fun ImagePickerGrid(
    images: List<Uri>,
    onAddClick: () -> Unit,
    onDelete: (Int) -> Unit,
    onReorder: (from: Int, to: Int) -> Unit,
    modifier: Modifier = Modifier,
    header: (@Composable () -> Unit)? = null,
) {
    val lazyGridState = rememberLazyGridState()
    // 드래그 재정렬: from/to는 이미지 셀 key(URI 문자열)를 통해 원본 인덱스로 변환
    val reorderableState = rememberReorderableLazyGridState(lazyGridState) { from, to ->
        val fromKey = from.key as? String ?: return@rememberReorderableLazyGridState
        val toKey = to.key as? String ?: return@rememberReorderableLazyGridState
        val fromIndex = images.indexOfFirst { it.toString() == fromKey }
        val toIndex = images.indexOfFirst { it.toString() == toKey }
        if (fromIndex >= 0 && toIndex >= 0) onReorder(fromIndex, toIndex)
    }

    LazyVerticalGrid(
        state = lazyGridState,
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(11.dp),
        verticalArrangement = Arrangement.spacedBy(11.dp),
    ) {
        if (header != null) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                header()
            }
        }
        item(key = "add_cell") {
            AddImageCell(onClick = onAddClick)
        }
        itemsIndexed(items = images, key = { _, uri -> uri.toString() }) { index, uri ->
            ReorderableItem(reorderableState, key = uri.toString()) { _ ->
                ImageCell(
                    uri = uri,
                    onDelete = { onDelete(index) },
                    modifier = Modifier.longPressDraggableHandle(),
                )
            }
        }
    }
}

@Composable
private fun AddImageCell(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .aspectRatio(148 / 124f)
            .clip(RoundedCornerShape(8.dp))
            .background(LIGHT_GRAY)
            .noRippleClickable(onClick),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_plus),
            contentDescription = "이미지 추가",
            modifier = Modifier.size(24.dp),
        )
    }
}

@Composable
private fun ImageCell(
    uri: Uri,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .aspectRatio(148 / 124f)
            .clip(RoundedCornerShape(8.dp)),
    ) {
        YogheeImage(
            model = uri,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Image(
            painter = painterResource(R.drawable.ic_close),
            contentDescription = "이미지 삭제",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 8.dp, top = 8.dp)
                .size(12.dp)
                .noRippleClickable(onDelete),
        )
    }
}
