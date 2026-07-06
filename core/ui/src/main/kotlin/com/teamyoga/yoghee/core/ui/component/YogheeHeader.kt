package com.teamyoga.yoghee.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.GRAY
import com.teamyoga.yoghee.core.ui.util.noRippleClickable

@Composable
fun YogheeHeader(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    subTitle: String = "",
    onSubTitleClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(top = 22.dp, bottom = 14.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.ic_back),
            contentDescription = stringResource(R.string.common_back),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 24.dp)
                .width(23.dp)
                .height(20.dp)
                .noRippleClickable(onBack),
        )
        YogheeText(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = BLACK,
            modifier = Modifier.align(Alignment.Center),
        )
        if (subTitle.isNotEmpty()) {
            YogheeText(
                text = subTitle,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = GRAY,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 17.dp)
                    .noRippleClickable(onSubTitleClick),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun YogheeHeaderPreview() {
    YogheeHeader(title = "타이틀", onBack = {})
}

@Preview(showBackground = true, name = "YogheeHeader with subTitle")
@Composable
private fun YogheeHeaderWithSubTitlePreview() {
    YogheeHeader(
        title = "수련 설명",
        onBack = {},
        subTitle = "문의 하기",
        onSubTitleClick = {},
    )
}
