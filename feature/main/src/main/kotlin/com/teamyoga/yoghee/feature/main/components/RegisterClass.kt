package com.teamyoga.yoghee.feature.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamyoga.yoghee.core.ui.R

@Composable
fun RegisterClass(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.img_banner_register_class),
        contentDescription = null,
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .aspectRatio(328f / 132f)
            .clip(RoundedCornerShape(8.dp))
    )
}

@Preview
@Composable
fun RegisterClassPreview() {
    RegisterClass()
}