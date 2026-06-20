package com.teamyoga.yoghee.feature.category.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamyoga.yoghee.core.ui.theme.MIND_ORANGE
import com.teamyoga.yoghee.core.ui.theme.WHITE

@Composable
fun CategoryAdBanner(text: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(MIND_ORANGE, shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        text = text,
        color = WHITE,
        textAlign = TextAlign.Center
    )
}

@Preview
@Composable
fun adBannerPreview() {
    CategoryAdBanner("할인해요")
}