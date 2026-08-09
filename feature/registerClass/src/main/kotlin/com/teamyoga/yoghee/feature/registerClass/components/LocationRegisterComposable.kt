package com.teamyoga.yoghee.feature.registerClass.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.GRAY
import com.teamyoga.yoghee.core.ui.theme.LIGHT_GRAY
import com.teamyoga.yoghee.core.ui.theme.MIND_ORANGE
import com.teamyoga.yoghee.core.ui.theme.WHITE
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme
import com.teamyoga.yoghee.core.ui.util.noRippleClickable

@Composable
fun LocationRegisterButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(WHITE)
            .border(width = 1.dp, color = MIND_ORANGE, shape = RoundedCornerShape(8.dp))
            .noRippleClickable(onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        YogheeText(
            text = "새 요가원 등록하기",
            color = MIND_ORANGE,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
        )
        YogheeText(
            text = "등록하신 수련 장소가 없나요?\n새 요가원을 등록 해주세요.",
            color = BLACK,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
fun LocationListItem(
    date: String,
    name: String,
    location: String,
    modifier: Modifier = Modifier,
    onAddClass: () -> Unit = {},
    onEditAddress: () -> Unit = {},
    onClose: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(WHITE)
            .border(width = 1.dp, color = LIGHT_GRAY, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            YogheeText(
                text = date,
                color = GRAY,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f),
            )
            Image(
                painter = painterResource(com.teamyoga.yoghee.core.ui.R.drawable.ic_close),
                contentDescription = "닫기",
                modifier = Modifier
                    .size(16.dp)
                    .noRippleClickable(onClose),
            )
        }
        YogheeText(
            text = name,
            color = BLACK,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
        )
        HorizontalDivider(thickness = 1.dp, color = LIGHT_GRAY)
        YogheeText(
            text = location,
            color = BLACK,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            LocationActionButton(
                text = "수련 추가 등록",
                onClick = onAddClass,
                modifier = Modifier
                    .weight(1f)
                    .background(color = LIGHT_GRAY, shape = RoundedCornerShape(8.dp)),
            )
            LocationActionButton(
                text = "주소 수정",
                onClick = onEditAddress,
                modifier = Modifier
                    .weight(1f)
                    .border(width = 1.dp, color = LIGHT_GRAY, shape = RoundedCornerShape(8.dp)),
            )
        }
    }
}

@Composable
private fun LocationActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .noRippleClickable(onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        YogheeText(
            text = text,
            color = BLACK,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Preview(showBackground = true, name = "LocationRegisterButton")
@Composable
private fun LocationRegisterButtonPreview() {
    YogheeTheme {
        LocationRegisterButton(
            onClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, name = "LocationListItem")
@Composable
private fun LocationListItemPreview() {
    YogheeTheme {
        LocationListItem(
            date = "2026-08-01",
            name = "정환요가원",
            location = "경기 남양주시 다산중앙로123번길 22-26 899호",
            modifier = Modifier.padding(16.dp),
        )
    }
}
