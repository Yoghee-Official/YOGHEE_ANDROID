package com.teamyoga.yoghee.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.theme.GRAY_D9D9D9
import com.teamyoga.yoghee.core.ui.theme.MIND_ORANGE

/**
 * 이미지 리스트를 좌우로 스와이프하며 보여주는 페이저.
 * 이미지가 2장 이상일 때만 하단에 원형 인디케이터를 표시한다.
 */

@Composable
fun YogheeImagePager(
    images: List<String>,
    isFavorite: Boolean,
    contentDescription: String? = null,
    cornerRadius: Dp = 8.dp,
) {
    // 이미지가 없으면 fallback 한 장만 보이도록 pageCount 1로 처리.
    val pageCount = images.size.coerceAtLeast(1)
    val pagerState = rememberPagerState(pageCount = { pageCount })

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(328f / 211f)
            .clip(RoundedCornerShape(cornerRadius)),
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            YogheeImage(
                model = images.getOrNull(page),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }

        PageIndicator(
            pageCount = images.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
        )

        // 책갈피 아이콘
        Image(
            painter = painterResource(
                id = if (isFavorite) R.drawable.ic_flag_selected else R.drawable.ic_flag_unselected,
            ),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 16.dp)
                .width(25.dp)
                .height(30.dp),
        )

        // todo:: 하단 플래그
    }
}

@Composable
private fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        repeat(pageCount) { index ->
            val isActive = index == currentPage
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(if (isActive) MIND_ORANGE else GRAY_D9D9D9),
            )
        }
    }
}

@Preview(widthDp = 360)
@Composable
private fun YogheeImagePagerMultiPreview() {
    YogheeImagePager(
        images = listOf("a", "b", "c"),
        isFavorite = false,
    )
}