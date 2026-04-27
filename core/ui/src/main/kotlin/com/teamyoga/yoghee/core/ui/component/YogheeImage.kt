package com.teamyoga.yoghee.core.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun YogheeImage(
    model: Any?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.FillWidth,
    contentDescription: String? = null,
) {
    val fallbackPainter = ColorPainter(Color.LightGray)
    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        contentScale = contentScale,
        placeholder = fallbackPainter,
        error = fallbackPainter,
        fallback = fallbackPainter,
        modifier = modifier,
    )
}
