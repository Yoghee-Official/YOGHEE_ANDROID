package com.teamyoga.yoghee.feature.main.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.teamyoga.yoghee.core.ui.theme.*
import com.teamyoga.yoghee.core.ui.R

@Composable
fun MainHeader() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(

        ) {
            Image(
                painter = painterResource(id = R.drawable.header_logo),
                contentDescription = "YOGHEE 로고",
                modifier = Modifier
                    .padding(start = 25.dp, top = 10.dp)
                    .size(width = 107.dp, height = 24.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
//                CategoryToggle()
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(id = R.drawable.ic_bird),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 4.dp)
                        .size(width = 30.dp, height = 24.dp)
                )
            }
        }
    }
}

//@Composable
//fun CategoryToggle(checked: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
//    Surface(
//        modifier = modifier
//            .clip(RoundedCornerShape(100.dp))
//            .clickable { onCheckedChange(!checked) }, // 클릭 시 상태 반전
//        color = White,
//        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
//        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp)
//    ) {
//        Row() {
//            Text(
//                text = text,
//                color = contentColor,
//                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
//                style = MaterialTheme.typography.labelLarge
//            )
//            Text(
//                text = text,
//                color = contentColor,
//                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
//                style = MaterialTheme.typography.labelLarge
//            )
//        }
//    }
//}