package com.teamyoga.yoghee.feature.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamyoga.yoghee.core.ui.theme.GRAY_D9D9D9
import com.teamyoga.yoghee.core.ui.theme.MIND_ORANGE

private const val MAX_DOT_COUNT = 5

/**
 * 인디케이터 규칙:
 * - 최대 5개 노출.
 * - 이미지 수가 5 이하이면 이미지 수만큼 표시하고, 활성 점은 currentPage 를 그대로 따른다.
 * - 이미지 수가 5 초과이면 5개만 표시:
 *   · currentPage 0~2 → 해당 인덱스 활성
 *   · currentPage 3 ~ (total - 2) → 4번째 점(index 3) 유지
 *   · currentPage == total - 1 → 5번째 점(index 4) 활성
 * - 활성 색: MIND_ORANGE, 비활성 색: GRAY_D9D9D9
 */
@Composable
fun PagerIndicator(
    totalPages: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = MIND_ORANGE,
    inactiveColor: Color = GRAY_D9D9D9,
    dotSize: Int = 8,
    dotSpacing: Int = 4,
) {
    val dotCount = totalPages.coerceAtMost(MAX_DOT_COUNT)
    val activeIndex = resolveActiveDotIndex(totalPages = totalPages, currentPage = currentPage)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(dotSpacing.dp),
    ) {
        repeat(dotCount) { index ->
            Box(
                modifier = Modifier
                    .size(dotSize.dp)
                    .clip(CircleShape)
                    .background(if (index == activeIndex) activeColor else inactiveColor),
            )
        }
    }
}

private fun resolveActiveDotIndex(totalPages: Int, currentPage: Int): Int {
    if (totalPages <= MAX_DOT_COUNT) return currentPage.coerceIn(0, totalPages - 1)
    return when {
        currentPage == totalPages - 1 -> MAX_DOT_COUNT - 1 // 마지막 페이지 → 5번째 점
        currentPage >= MAX_DOT_COUNT - 2 -> MAX_DOT_COUNT - 2 // 4번째 점 유지 (index 3)
        else -> currentPage
    }
}

@Preview(showBackground = true)
@Composable
private fun PagerIndicator3PagesFirstPreview() {
    PagerIndicator(totalPages = 3, currentPage = 0)
}

@Preview(showBackground = true)
@Composable
private fun PagerIndicator5PagesLastPreview() {
    PagerIndicator(totalPages = 5, currentPage = 4)
}

@Preview(showBackground = true)
@Composable
private fun PagerIndicator8PagesMiddlePreview() {
    // total=8, currentPage=5 → 4번째 점 유지
    PagerIndicator(totalPages = 8, currentPage = 5)
}

@Preview(showBackground = true)
@Composable
private fun PagerIndicator8PagesLastPreview() {
    // total=8, currentPage=7 → 5번째 점 활성
    PagerIndicator(totalPages = 8, currentPage = 7)
}
