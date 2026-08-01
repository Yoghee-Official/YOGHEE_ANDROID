package com.teamyoga.yoghee.feature.registerClass.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.GRAY
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme

@Composable
fun RegisterSectionTitle(
    title: String,
    modifier: Modifier = Modifier,
    subTitle: String? = null,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        YogheeText(
            text = title,
            color = BLACK,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp),
        )
        if (subTitle != null) {
            YogheeText(
                text = subTitle,
                color = GRAY,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp, start = 8.dp),
            )
        }
    }
}

@Preview(showBackground = true, name = "RegisterSectionTitle")
@Composable
private fun RegisterSectionTitlePreview() {
    YogheeTheme {
        RegisterSectionTitle(
            title = "수련에 대해 알려주세요.",
            subTitle = "[마이페이지]->[개선 수련 목록]에서 수정할 수 있습니다.",
            modifier = Modifier.padding(24.dp),
        )
    }
}
