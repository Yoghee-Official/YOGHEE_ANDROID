package com.teamyoga.yoghee.feature.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamyoga.yoghee.core.ui.component.YogheeImage
import com.teamyoga.yoghee.core.ui.theme.GRAY_D9D9D9

/**
 * 상세 화면 대표 이미지 영역.
 * - 1:1 비율의 HorizontalPager (끝이 있는 스와이프)
 * - 하단 중앙에 PagerIndicator 오버레이
 * - 이미지 클릭 시 onImageClick (팝업은 후속)
 */
@Composable
fun DetailImagePager(
    images: List<String>,
    onImageClick: (page: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val totalPages = images.size.coerceAtLeast(1)
    val pagerState = rememberPagerState(pageCount = { totalPages })

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(GRAY_D9D9D9),
    ) {
        if (images.isEmpty()) {
            // 이미지 미제공 시 회색 배경만 노출
            return@Box
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            YogheeImage(
                model = images[page],
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) {
                        onImageClick(page)
                    },
            )
        }
        PagerIndicator(
            totalPages = totalPages,
            currentPage = pagerState.currentPage,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 36.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailImagePagerPreview() {
    DetailImagePager(
        images = listOf("https://example.com/1.jpg", "https://example.com/2.jpg"),
        onImageClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun DetailImagePagerEmptyPreview() {
    DetailImagePager(
        images = emptyList(),
        onImageClick = {},
    )
}
