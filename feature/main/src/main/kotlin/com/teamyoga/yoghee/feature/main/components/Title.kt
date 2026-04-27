package com.teamyoga.yoghee.feature.main.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.theme.BLACK

@Composable
fun Title(
    title: String,
    modifier: Modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
) {
    Text(
        modifier = modifier,
        text = title,
        color = BLACK,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
}