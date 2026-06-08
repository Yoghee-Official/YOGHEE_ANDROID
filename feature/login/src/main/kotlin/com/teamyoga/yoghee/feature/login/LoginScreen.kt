package com.teamyoga.yoghee.feature.login

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.GRAY
import com.teamyoga.yoghee.core.ui.theme.KAKAO_YELLOW
import com.teamyoga.yoghee.core.ui.theme.LIGHT_GRAY_F2F2F2
import com.teamyoga.yoghee.core.ui.theme.MIND_ORANGE
import com.teamyoga.yoghee.core.ui.theme.NAVER_GREEN
import com.teamyoga.yoghee.core.ui.theme.WHITE

@Composable
fun LoginScreen(
    onKakaoClick: () -> Unit,
    onNaverClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onAppleClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 72.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(132.dp))
        Image(
            painter = painterResource(id = R.drawable.header_logo),
            contentDescription = "로고",
            modifier = Modifier.size(width = 120.dp, height = 24.dp)
        )

        Spacer(modifier = Modifier.height(44.dp))

        Text(
            text = "간편하게",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MIND_ORANGE
        )

        Text(
            text = "로그인 해보세요!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = BLACK
        )

        Spacer(modifier = Modifier.height(54.dp))

        SocialLoginButton(
            text = "Apple 로그인",
            backgroundColor = BLACK,
            contentColor = WHITE,
            icon = R.drawable.logo_login_apple,
            onClick = onAppleClick,
        )
        Spacer(modifier = Modifier.height(20.dp))
        SocialLoginButton(
            text = "Google 로그인",
            backgroundColor = LIGHT_GRAY_F2F2F2,
            contentColor = BLACK,
            icon = R.drawable.logo_login_google,
            onClick = onGoogleClick,
        )

        Spacer(modifier = Modifier.height(20.dp))

        SocialLoginButton(
            text = "네이버 로그인",
            backgroundColor = NAVER_GREEN,
            contentColor = Color.White,
            icon = R.drawable.logo_login_naver,
            onClick = onNaverClick,
        )

        Spacer(modifier = Modifier.height(20.dp))


        SocialLoginButton(
            text = "카카오 로그인",
            backgroundColor = KAKAO_YELLOW,
            contentColor = BLACK,
            icon = R.drawable.logo_login_kakao,
            onClick = onKakaoClick,
        )

        Spacer(modifier = Modifier.height(28.dp))
        Row() {
            Text(
                text = "이메일로 가입하기",
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                color = GRAY
            )
            Spacer(modifier = Modifier.width(24.dp))
            VerticalDivider(
                modifier = Modifier.height(12.dp),
                thickness = 1.dp,
                color = GRAY
            )
            Spacer(modifier = Modifier.width(24.dp))

            Text(
                text = "이메일로 로그인",
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                color = GRAY
            )
        }
    }
}

@Composable
private fun SocialLoginButton(
    text: String,
    backgroundColor: Color,
    contentColor: Color,
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(216.dp)
            .height(48.dp),
        shape = RoundedCornerShape(100.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
        ),
        contentPadding = PaddingValues(horizontal = 20.dp),
    ) {
        Row(modifier = Modifier.width(101.dp)) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = "로고",
                modifier = Modifier.size(width = 18.dp, height = 18.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor,
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(
        onKakaoClick = {},
        onNaverClick = {},
        onGoogleClick = {},
        onAppleClick = {},
    )
}