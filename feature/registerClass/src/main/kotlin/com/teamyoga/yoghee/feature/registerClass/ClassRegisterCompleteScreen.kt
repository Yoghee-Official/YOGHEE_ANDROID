package com.teamyoga.yoghee.feature.registerClass

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.MIND_ORANGE
import com.teamyoga.yoghee.core.ui.theme.SAND_BEIGE
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme
import com.teamyoga.yoghee.core.ui.util.noRippleClickable

@Composable
fun ClassRegisterCompleteScreen(
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // 완료 화면에서 뒤로가기는 마이페이지 이동과 동일하게 처리
    BackHandler(onBack = onDone)

    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .background(SAND_BEIGE),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            YogheeText(
                text = buildAnnotatedString {
                    append("나마스떼\uD83D\uDE4F\n수련 등록이 ")
                    withStyle(style = SpanStyle(color = MIND_ORANGE)) {
                        append("완료")
                    }
                    append("되었어요.")
                },
                color = BLACK,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 84.dp, end = 84.dp, top = 8.dp, bottom = 32.dp)
                .height(48.dp)
                .paint(
                    painter = painterResource(R.drawable.btn_continue_class_register),
                    contentScale = ContentScale.FillBounds,
                )
                .noRippleClickable(onDone),
            contentAlignment = Alignment.Center,
        ) {
            YogheeText(
                text = "완료",
                color = BLACK,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "ClassRegisterCompleteScreen")
@Composable
private fun ClassRegisterCompleteScreenPreview() {
    YogheeTheme {
        ClassRegisterCompleteScreen(onDone = {})
    }
}
