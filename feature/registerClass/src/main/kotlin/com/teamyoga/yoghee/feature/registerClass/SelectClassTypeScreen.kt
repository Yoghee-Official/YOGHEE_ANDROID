package com.teamyoga.yoghee.feature.registerClass

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.component.YogheeHeader
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.MIND_ORANGE
import com.teamyoga.yoghee.core.ui.theme.SAND_BEIGE
import com.teamyoga.yoghee.core.ui.theme.WHITE
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme
import com.teamyoga.yoghee.core.ui.util.noRippleClickable

private data class ClassType(
    val labelRes: Int,
    val text1Res: Int,
    val text2Res: Int,
)

/**
 * TR/BL을 시작·끝 스톱, TL/BR을 중간 스톱에 정확히 배치하는 커스텀 브러시.
 * 축 방향은 TL-BR 대각선에 수직이며, 그리는 시점의 실제 크기를 받아 계산한다.
 */
private class DiagonalCornerBrush(
    private val colors: List<Color>,
) : ShaderBrush() {
    override fun createShader(size: Size): Shader {
        val w = size.width
        val h = size.height
        val k2 = 2f * (w * w + h * h)
        return LinearGradientShader(
            from = Offset(
                x = w * (w * w - h * h) / k2,
                y = h * (3f * w * w + h * h) / k2,
            ),
            to = Offset(
                x = w * (w * w + 3f * h * h) / k2,
                y = h * (h * h - w * w) / k2,
            ),
            colors = colors,
        )
    }
}

private val classTypes = listOf(
    ClassType(
        labelRes = R.string.class_type_oneday,
        text1Res = R.string.class_type_oneday_text1,
        text2Res = R.string.class_type_oneday_text2,
    ),
    ClassType(
        labelRes = R.string.class_type_regular,
        text1Res = R.string.class_type_regular_text1,
        text2Res = R.string.class_type_regular_text2,
    ),
    ClassType(
        labelRes = R.string.class_type_season,
        text1Res = R.string.class_type_season_text1,
        text2Res = R.string.class_type_season_text2,
    ),
    ClassType(
        labelRes = R.string.class_type_workshop,
        text1Res = R.string.class_type_workshop_text1,
        text2Res = R.string.class_type_workshop_text2,
    ),
)

@Composable
fun SelectClassTypeScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SAND_BEIGE),
    ) {
        YogheeHeader(
            title = stringResource(R.string.select_class_type_title),
            onBack = onBack
        )
        SelectClassTypeContent()
    }
}

@Composable
private fun SelectClassTypeContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        YogheeText(
            text = stringResource(R.string.select_class_type_description),
            color = BLACK,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 48.dp)
        )

        val pagerState = rememberPagerState(pageCount = { classTypes.size })
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 8.dp),
            pageSpacing = 8.dp,
            modifier = Modifier.fillMaxWidth(),
        ) { page ->
            ClassTypeBanner(
                type = classTypes[page],
                onClick = {},
            )
        }

        YogheeText(
            text = stringResource(R.string.select_class_type_notice),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = BLACK,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun ClassTypeBanner(
    type: ClassType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(327f / 409f)
            .clip(RoundedCornerShape(20.dp))
            .border(
                width = 1.dp,
                color = WHITE,
                shape = RoundedCornerShape(20.dp),
            )
            .noRippleClickable(onClick = onClick)
            .padding(8.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.img_class_type_banner),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
            )
            val circleId = "circle"
            val spacingId = "spacing"
            val labelWithCircle = buildAnnotatedString {
                appendInlineContent(circleId)
                appendInlineContent(spacingId)
                append("다양한 요기니와 만나고 싶다면 추천해요")
            }
            val labelInlineContent = mapOf(
                circleId to InlineTextContent(
                    Placeholder(
                        width = 8.sp,
                        height = 8.sp,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.Center,
                    ),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(MIND_ORANGE.copy(alpha = 0.8f)),
                    )
                },
                spacingId to InlineTextContent(
                    Placeholder(
                        width = 4.sp,
                        height = 6.sp,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.Center,
                    ),
                ) {},
            )
            YogheeText(
                text = labelWithCircle,
                inlineContent = labelInlineContent,
                color = BLACK,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(top = 12.dp, start = 12.dp)
                    .align(Alignment.TopStart)
                    .clip(RoundedCornerShape(31.dp))
                    .background(WHITE.copy(alpha = 0.8f))
                    .border(width = 1.dp, color = WHITE, shape = RoundedCornerShape(31.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            ) {
                YogheeText(
                    text = stringResource(type.labelRes),
                    color = WHITE,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                YogheeText(
                    text = stringResource(type.text1Res),
                    color = WHITE,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                YogheeText(
                    text = stringResource(type.text2Res),
                    color = WHITE,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 6.dp)
                )

                YogheeText(
                    text = "수련 개설하기",
                    color = WHITE,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 19.dp, bottom = 16.dp)
                        .fillMaxWidth()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    WHITE.copy(alpha = 0f),
                                    WHITE.copy(alpha = 0.3f),
                                ),
                            ),
                            shape = RoundedCornerShape(40.dp),
                        )
                        .border(
                            width = 1.dp,
                            brush = DiagonalCornerBrush(
                                colors = listOf(
                                    WHITE.copy(alpha = 0f),
                                    WHITE.copy(alpha = 1f),
                                    WHITE.copy(alpha = 0f),
                                ),
                            ),
                            shape = RoundedCornerShape(40.dp),
                        )
                        .padding(vertical = 11.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "SelectClassTypeScreen")
@Composable
private fun SelectClassTypeScreenPreview() {
    YogheeTheme {
        SelectClassTypeScreen(onBack = {})
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "ClassTypeBanner")
@Composable
private fun ClassTypeBannerPreview() {
    YogheeTheme {
        Box(modifier = Modifier.background(SAND_BEIGE).padding(16.dp)) {
            ClassTypeBanner(type = classTypes[0], onClick = {})
        }
    }
}
