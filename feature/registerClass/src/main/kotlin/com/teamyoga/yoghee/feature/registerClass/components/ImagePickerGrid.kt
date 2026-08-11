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

// 같은 URI가 여러 번 추가되어도 LazyLayout의 key 충돌이 나지 않도록
// URI를 고유 id로 래핑한다.
data class ImageItem(
    val id: String,
    val uri: Uri,
)

@Composable
fun ImagePickerGrid(
    images: List<ImageItem>,
    onAddClick: () -> Unit,
    onDelete: (Int) -> Unit,
    onReorder: (from: Int, to: Int) -> Unit,
    modifier: Modifier = Modifier,
    header: (@Composable () -> Unit)? = null,
) {
    val lazyGridState = rememberLazyGridState()
    // 드래그 재정렬: from/to key(ImageItem.id)를 통해 원본 인덱스로 변환
    val reorderableState = rememberReorderableLazyGridState(lazyGridState) { from, to ->
        val fromKey = from.key as? String ?: return@rememberReorderableLazyGridState
        val toKey = to.key as? String ?: return@rememberReorderableLazyGridState
        val fromIndex = images.indexOfFirst { it.id == fromKey }
        val toIndex = images.indexOfFirst { it.id == toKey }
        if (fromIndex >= 0 && toIndex >= 0) onReorder(fromIndex, toIndex)
    }

    LazyVerticalGrid(
        state = lazyGridState,
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(11.dp),
        verticalArrangement = Arrangement.spacedBy(11.dp),
    ) {
        item(key = "add_cell") {
            AddImageCell(onClick = onAddClick)
        }
        itemsIndexed(items = images, key = { _, item -> item.id }) { index, item ->
            ReorderableItem(reorderableState, key = item.id) { _ ->
                ImageCell(
                    uri = item.uri,
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
