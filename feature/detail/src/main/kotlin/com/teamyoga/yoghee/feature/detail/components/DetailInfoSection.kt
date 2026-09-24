package com.teamyoga.yoghee.feature.detail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.domain.model.ClassCenter
import com.teamyoga.yoghee.core.domain.model.ClassFeature
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.GRAY
import com.teamyoga.yoghee.core.ui.theme.GRAY_D9D9D9
import com.teamyoga.yoghee.core.ui.theme.LIGHT_GRAY
import com.teamyoga.yoghee.core.ui.theme.LIGHT_GRAY_F2F2F2
import com.teamyoga.yoghee.core.ui.theme.WHITE

private const val MAX_RECOMMEND_FEATURE_COUNT = 3
private const val MAX_REVIEW_COUNT_DISPLAY = 99_999

/**
 * 상세 화면 정보 영역 (3a ~ 3d)
 */
@Composable
fun DetailInfoSection(
    className: String,
    rating: Double,
    reviewCount: Int,
    center: ClassCenter?,
    features: List<ClassFeature>,
    onRatingClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 32.dp),
    ) {
        // 3a 수련명 + 3b 리뷰 평점/개수
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            ClassNameText(
                name = className,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(44.dp))
            ReviewRatingColumn(
                rating = rating,
                reviewCount = reviewCount,
                onClick = onRatingClick,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3c 요가원 주소
        Text(
            text = formatCenterAddress(center),
            fontSize = 12.sp,
            color = BLACK,
            maxLines = 2,
            fontWeight = FontWeight.Medium,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3d 요가 추천 텍스트
        RecommendFeatureRow(features = features)
    }
}

/**
 * 3a 수련명
 * - maxLines = 2 + Ellipsis. 컨테이너 너비 기반으로 자연스럽게 wrap.
 */
@Composable
private fun ClassNameText(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = name,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = BLACK,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier,
    )
}

/**
 * 3b 별점 (★ 0.0) + 리뷰 개수 텍스트. 세로 정렬로 표시하고 Row 전체 클릭 시 리뷰 영역 앵커링 콜백.
 */
@Composable
private fun ReviewRatingColumn(
    rating: Double,
    reviewCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick,
        ),
        horizontalAlignment = Alignment.End,
    ) {
        Text(
            text = "★ ${formatRating(rating)}",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = BLACK,
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = BLACK,
                    shape = RoundedCornerShape(36.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "리뷰수 ${formatReviewCount(reviewCount)}개",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = BLACK,
            textDecoration = TextDecoration.Underline,
        )
    }
}

/**
 * 3d 요가 추천 텍스트 리스트. LazyRow 로 끝이 있는 스와이프, 최대 3개.
 */
@Composable
private fun RecommendFeatureRow(
    features: List<ClassFeature>,
    modifier: Modifier = Modifier,
) {
    val visible = features.take(MAX_RECOMMEND_FEATURE_COUNT)
    if (visible.isEmpty()) return

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 0.dp),
    ) {
        items(visible) { feature ->
            RecommendFeatureChip(description = feature.description)
        }
    }
}

@Composable
private fun RecommendFeatureChip(
    description: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(shape = RoundedCornerShape(23.dp))
            .background(WHITE)
            .border(
                width = 1.dp,
                color = LIGHT_GRAY,
                shape = RoundedCornerShape(23.dp),
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_checked),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = description,
            fontSize = 10.sp,
            color = BLACK,
            maxLines = 1,
            fontWeight = FontWeight.Medium,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/**
 * 별점을 소수점 1자리로 노출. 예: 4.0, 4.8
 */
private fun formatRating(rating: Double): String = "%.1f".format(rating)

/**
 * 리뷰 개수 포맷. 99,999 초과 시 "99,999+".
 */
private fun formatReviewCount(count: Int): String =
    if (count > MAX_REVIEW_COUNT_DISPLAY) "99,999+" else "%,d".format(count)

/**
 * 시/군/구/상세주소를 공백으로 join. 빈 값은 스킵.
 */
private fun formatCenterAddress(center: ClassCenter?): String {
    if (center == null) return ""
    return listOf(center.sido, center.sigungu, center.roadAddress, center.detailAddress)
        .filter { it.isNotBlank() }
        .joinToString(" ")
}

@Preview(showBackground = true)
@Composable
private fun DetailInfoSectionPreview() {
    DetailInfoSection(
        className = "일이삼사오육칠팔구십일 이상사오육칠팔구십일이",
        rating = 4.0,
        reviewCount = 12_300,
        center = ClassCenter(
            centerId = "c1",
            name = "요가원",
            description = "",
            thumbnail = "",
            sido = "서울",
            sigungu = "영등포구",
            roadAddress = "당산동4가 40번길 40",
            jibunAddress = "",
            detailAddress = "3층",
            latitude = 0.0,
            longitude = 0.0,
            amenities = emptyList(),
            facilities = emptyList(),
        ),
        features = listOf(
            ClassFeature(code = "F1", description = "요가 입문자용"),
            ClassFeature(code = "F2", description = "왕초보 반복 숙달"),
            ClassFeature(code = "F3", description = "요가 밸런스 스트레칭"),
        ),
        onRatingClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun DetailInfoSectionLargeReviewPreview() {
    DetailInfoSection(
        className = "짧은 수련명",
        rating = 4.9,
        reviewCount = 123_456,
        center = ClassCenter(
            centerId = "c2",
            name = "요가원",
            description = "",
            thumbnail = "",
            sido = "서울",
            sigungu = "강남구",
            roadAddress = "테헤란로 123",
            jibunAddress = "",
            detailAddress = "",
            latitude = 0.0,
            longitude = 0.0,
            amenities = emptyList(),
            facilities = emptyList(),
        ),
        features = listOf(
            ClassFeature(code = "F1", description = "초급"),
        ),
        onRatingClick = {},
    )
}
