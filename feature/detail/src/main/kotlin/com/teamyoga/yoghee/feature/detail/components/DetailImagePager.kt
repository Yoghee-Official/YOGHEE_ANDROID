package com.teamyoga.yoghee.feature.detail.components

import android.graphics.drawable.PaintDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.teamyoga.yoghee.core.ui.component.YogheeImage
import com.teamyoga.yoghee.core.ui.theme.GRAY_D9D9D9
import com.teamyoga.yoghee.core.ui.theme.MIND_ORANGE
import com.teamyoga.yoghee.core.ui.theme.WHITE
import com.teamyoga.yoghee.core.ui.util.noRippleClickable
import com.teamyoga.yoghee.feature.detail.R

/**
 * 상세 화면 대표 이미지 영역.
 * - 1:1 비율의 HorizontalPager (끝이 있는 스와이프)
 * - 하단 중앙에 PagerIndicator 오버레이
 * - 이미지 클릭 시 전체 화면 이미지 뷰어 노출
 */
@Composable
fun DetailImagePager(
    images: List<String>,
    modifier: Modifier = Modifier,
) {
    val totalPages = images.size.coerceAtLeast(1)
    val pagerState = rememberPagerState(pageCount = { totalPages })
    var viewerPage by remember(images) { mutableStateOf<Int?>(null) }

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
                        viewerPage = page
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

    viewerPage?.let { initialPage ->
        FullscreenImageViewer(
            images = images,
            initialPage = initialPage,
            onDismiss = { viewerPage = null },
        )
    }
}

@Composable
private fun FullscreenImageViewer(
    images: List<String>,
    initialPage: Int,
    onDismiss: () -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = initialPage.coerceIn(images.indices),
        pageCount = { images.size },
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(androidx.compose.ui.graphics.Color.Black),
        ) {
            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
                YogheeImage(
                    model = images[page],
                    contentDescription = "이미지 ${page + 1}",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            Image(
                painter = painterResource(com.teamyoga.yoghee.core.ui.R.drawable.ic_close_white),
                contentDescription = "닫기",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 83.dp, end = 20.dp)
                    .size(20.dp)
                    .noRippleClickable(onClick = onDismiss)
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 200.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                images.indices.forEach { page ->
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .background(
                                color = if (page == pagerState.currentPage) {
                                    MIND_ORANGE
                                } else {
                                    WHITE
                                },
                                shape = CircleShape,
                            ),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailImagePagerPreview() {
    DetailImagePager(
        images = listOf("https://example.com/1.jpg", "https://example.com/2.jpg"),
    )
}

@Preview(showBackground = true)
@Composable
private fun DetailImagePagerEmptyPreview() {
    DetailImagePager(
        images = emptyList(),
    )
}
