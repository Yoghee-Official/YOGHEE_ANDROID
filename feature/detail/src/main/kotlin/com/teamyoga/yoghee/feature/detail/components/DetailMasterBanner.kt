package com.teamyoga.yoghee.feature.detail.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.domain.model.ClassMasterInfo
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.component.YogheeImage
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.theme.BLACK_50
import com.teamyoga.yoghee.core.ui.theme.LAND_BROWN
import com.teamyoga.yoghee.core.ui.theme.LIGHT_GRAY
import com.teamyoga.yoghee.core.ui.theme.WHITE

private const val FLIP_ANIMATION_DURATION_MS = 600
private const val FLIP_HALF_ANGLE = 90f
private const val FLIP_FULL_ANGLE = 180f

/**
 * 4a 대표 강사 배너 + 4aa 뒤집기 버튼
 * - 1:1 정사각형
 * - 4aa 버튼 클릭 시 Y축(좌우) 3D 플립 애니메이션으로 앞/뒷면 전환
 * - 앞면: 프로필 이미지 + 강사 닉네임 + 소개(introduction)
 * - 뒷면: certificate, career, introduction 순 노출
 */
@Composable
fun DetailMasterBanner(
    masterInfo: ClassMasterInfo,
    modifier: Modifier = Modifier,
) {
    var flipped by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (flipped) FLIP_FULL_ANGLE else 0f,
        animationSpec = tween(durationMillis = FLIP_ANIMATION_DURATION_MS),
        label = "master_banner_flip",
    )
    // 회전이 90도 넘어가면 뒷면 표시. 뒷면은 rotationY 를 -180 보정해 텍스트가 정상 방향으로 노출되게 함.
    val showBack = rotation > FLIP_HALF_ANGLE

    Box(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .aspectRatio(328 / 280f)
            .clip(RoundedCornerShape(20.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    rotationY = if (showBack) rotation - FLIP_FULL_ANGLE else rotation
                    cameraDistance = 12f * density
                },
        ) {
            if (showBack) {
                MasterBannerBack(masterInfo = masterInfo)
            } else {
                MasterBannerFront(masterInfo = masterInfo)
            }
        }
        // 4aa 뒤집기 버튼. 배너와 함께 회전하지 않도록 회전 컨테이너 바깥에 배치.
        FlipButton(
            onClick = { flipped = !flipped },
            modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp)
        )
    }
}

@Composable
private fun MasterBannerFront(masterInfo: ClassMasterInfo) {
    Box(modifier = Modifier.fillMaxSize()) {
        YogheeImage(
            model = masterInfo.profileImage,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        // 하단 텍스트 가독성 확보용 어두운 그라디언트
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                    ),
                )
                .padding(horizontal = 24.dp, vertical = 24.dp),
        ) {
            Text(
                text = masterInfo.nickname,
                color = WHITE,
                fontSize = 12.sp,
                maxLines = 1,
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = masterInfo.introduction,
                color = WHITE,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun MasterBannerBack(masterInfo: ClassMasterInfo) {
    Box(modifier = Modifier.fillMaxSize()) {
        YogheeImage(
            model = masterInfo.profileImage,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        // 하단 텍스트 가독성 확보용 어두운 그라디언트
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = BLACK_50)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MasterBannerBackRow(label = "대표 자격증명", value = masterInfo.certificate)
            MasterBannerBackRow(label = "총 요가 경력", value = formatCareer(masterInfo.career))
            MasterBannerBackRow(label = "소개말", value = masterInfo.introduction)
        }
    }
}

@Composable
private fun MasterBannerBackRow(label: String, value: String, maxLine: Int = 1) {
    Row {
        YogheeText(
            text = label,
            color = WHITE,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.width(80.dp)
        )
        YogheeText(
            text = value,
            color = WHITE,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            maxLines = maxLine,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun FlipButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(LAND_BROWN)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_arrow),
            contentDescription = "뒤집기",
            modifier = Modifier
                .width(25.dp)
                .height(20.dp),
        )
    }
}

private fun formatCareer(career: Int): String = "${career}년차"

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun DetailMasterBannerPreview() {
    DetailMasterBanner(
        masterInfo = ClassMasterInfo(
            nickname = "박지현 (INJIN)",
            profileImage = "https://picsum.photos/seed/yoghee-master/800/800",
            introduction = "일이삼사오육칠팔구십일 일이삼사오육칠팔구십",
            career = 10,
            certificate = "RYT 200",
        ),
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun DetailMasterBannerBackPreview() {
    // 뒷면 시각 확인용 (플립 상태를 직접 렌더링).
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(LIGHT_GRAY),
    ) {
        MasterBannerBack(
            masterInfo = ClassMasterInfo(
                nickname = "박지현 (INJIN)",
                profileImage = "",
                introduction = "요가 입문자를 위한 반복 숙달과 밸런스에 초점을 맞춥니다.",
                career = 10,
                certificate = "RYT 200",
            ),
        )
    }
}
