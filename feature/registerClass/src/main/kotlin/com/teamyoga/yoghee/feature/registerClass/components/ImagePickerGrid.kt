package com.teamyoga.yoghee.feature.registerClass.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.component.YogheeImage
import com.teamyoga.yoghee.core.ui.theme.LIGHT_GRAY
import com.teamyoga.yoghee.core.ui.theme.SAND_BEIGE
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme
import com.teamyoga.yoghee.core.ui.util.noRippleClickable

@Composable
fun ImagePickerGrid(
    images: List<Uri>,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
    header: (@Composable () -> Unit)? = null,
) {
    LazyVerticalGrid(
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
        item {
            AddImageCell(onClick = onAddClick)
        }
        items(items = images, key = { it.toString() }) { uri ->
            ImageCell(uri = uri)
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
            .aspectRatio(148/124f)
            .clip(RoundedCornerShape(8.dp))
            .background(LIGHT_GRAY)
            .noRippleClickable(onClick),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_plus),
            contentDescription = "이미지 추가",
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun ImageCell(
    uri: Uri,
    modifier: Modifier = Modifier,
) {
    YogheeImage(
        model = uri,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .aspectRatio(148/124f)
            .clip(RoundedCornerShape(8.dp)),
    )
}

@Preview(showBackground = true, name = "ImagePickerGrid Empty")
@Composable
private fun ImagePickerGridEmptyPreview() {
    YogheeTheme {
        Box(modifier = Modifier.background(SAND_BEIGE)) {
            ImagePickerGrid(
                images = emptyList(),
                onAddClick = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "ImagePickerGrid With Images")
@Composable
private fun ImagePickerGridWithImagesPreview() {
    YogheeTheme {
        Box(modifier = Modifier.background(SAND_BEIGE)) {
            ImagePickerGrid(
                images = List(5) { Uri.parse("preview://image/$it") },
                onAddClick = {},
            )
        }
    }
}
