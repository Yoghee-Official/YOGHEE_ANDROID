package com.teamyoga.yoghee.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.LineHeightStyle.Alignment
import androidx.compose.ui.text.style.LineHeightStyle.Trim
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.R

val Pretendard = FontFamily(
    Font(R.font.pretendard_thin, FontWeight.Thin),
    Font(R.font.pretendard_extra_light, FontWeight.ExtraLight),
    Font(R.font.pretendard_light, FontWeight.Light),
    Font(R.font.pretendard_regular, FontWeight.Normal),
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_semi_bold, FontWeight.SemiBold),
    Font(R.font.pretendard_bold, FontWeight.Bold),
    Font(R.font.pretendard_extra_bold, FontWeight.ExtraBold),
)

private val NoFontPadding = PlatformTextStyle(includeFontPadding = false)
private val TightLineHeight = LineHeightStyle(
    alignment = Alignment.Center,
    trim = Trim.Both,
)

val Typography = Typography(
    displayLarge = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Bold, fontSize = 57.sp, lineHeight = 64.sp, platformStyle = NoFontPadding, lineHeightStyle = TightLineHeight),
    displayMedium = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Bold, fontSize = 45.sp, lineHeight = 52.sp, platformStyle = NoFontPadding, lineHeightStyle = TightLineHeight),
    displaySmall = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Bold, fontSize = 36.sp, lineHeight = 44.sp, platformStyle = NoFontPadding, lineHeightStyle = TightLineHeight),
    headlineLarge = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.SemiBold, fontSize = 32.sp, lineHeight = 40.sp, platformStyle = NoFontPadding, lineHeightStyle = TightLineHeight),
    headlineMedium = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.SemiBold, fontSize = 28.sp, lineHeight = 36.sp, platformStyle = NoFontPadding, lineHeightStyle = TightLineHeight),
    headlineSmall = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.SemiBold, fontSize = 24.sp, lineHeight = 32.sp, platformStyle = NoFontPadding, lineHeightStyle = TightLineHeight),
    titleLarge = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 28.sp, platformStyle = NoFontPadding, lineHeightStyle = TightLineHeight),
    titleMedium = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Medium, fontSize = 16.sp, lineHeight = 24.sp, platformStyle = NoFontPadding, lineHeightStyle = TightLineHeight),
    titleSmall = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp, platformStyle = NoFontPadding, lineHeightStyle = TightLineHeight),
    bodyLarge = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.5.sp, platformStyle = NoFontPadding, lineHeightStyle = TightLineHeight),
    bodyMedium = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp, platformStyle = NoFontPadding, lineHeightStyle = TightLineHeight),
    bodySmall = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 16.sp, platformStyle = NoFontPadding, lineHeightStyle = TightLineHeight),
    labelLarge = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp, platformStyle = NoFontPadding, lineHeightStyle = TightLineHeight),
    labelMedium = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp, platformStyle = NoFontPadding, lineHeightStyle = TightLineHeight),
    labelSmall = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 16.sp, platformStyle = NoFontPadding, lineHeightStyle = TightLineHeight),
)
