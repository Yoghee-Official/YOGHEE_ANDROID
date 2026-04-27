package com.teamyoga.yoghee.feature.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.domain.model.NewReview
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.component.YogheeImage
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.GRAY
import com.teamyoga.yoghee.core.ui.theme.LIGHT_GRAY
import com.teamyoga.yoghee.core.ui.theme.WHITE
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun NewReviewSection(
    reviews: List<NewReview>,
    title: String = "최신 리뷰"
) {
    Column {
        Title(title = title)
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(reviews) { review ->
                NewReviewItem(review = review)
            }
        }
    }
}

@Composable
private fun NewReviewItem(
    review: NewReview
) {
    Column(
        modifier = Modifier
            .width(255.dp)
            .height(323.dp)
            .border(
                width = 1.dp,
                color = LIGHT_GRAY,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .background(WHITE)
            .padding(8.dp)
    ) {
        YogheeImage(
            model = review.thumbnail,
            modifier = Modifier
                .width(239.dp)
                .height(134.dp)
                .clip(RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            YogheeImage(
                model = review.userProfile,
                modifier = Modifier
                    .width(33.dp)
                    .height(33.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Column {
                Text(
                    text = review.nickname.orEmpty(),
                    fontSize = 12.sp,
                    color = BLACK,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Lv.${review.userLevel ?: 0}",
                    fontSize = 10.sp,
                    color = BLACK
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
            Text(   // date
                text = review.createdAt?.let { formatReviewDate(it) }.orEmpty(),
                fontSize = 10.sp,
                color = GRAY,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.weight(1f))
            review.rating?.let { StarScore(score = it) }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = review.content.orEmpty(),
            fontSize = 12.sp,
            color = BLACK,
            fontWeight = FontWeight.Medium,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            lineHeight = 12.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(   // 더보기
            text = stringResource(R.string.show_more),
            fontSize = 10.sp,
            color = GRAY,
            textDecoration = TextDecoration.Underline,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Right,
            modifier = Modifier.fillMaxWidth().padding(end = 8.dp)
        )
    }
}

@Composable
fun StarScore(score: Double) {
    val scoreToInt = score.toInt()
    Row {
        repeat(scoreToInt) {
            Image(
                painter = painterResource(id = R.drawable.ic_star_score),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 2.dp)
                    .width(14.dp)
                    .height(13.dp)
            )
        }
    }
}

private fun formatReviewDate(dateString: String): String = runCatching {
    val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        .parse(dateString)!!
    SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()).format(input)
}.getOrDefault(dateString)

@Preview(showBackground = true)
@Composable
fun NewReviewSectionPreview() {
    YogheeTheme {
        NewReviewSection(
            reviews = listOf(
                NewReview(
                    reviewId = "1",
                    userUuid = "uuid-001",
                    thumbnail = null,
                    content = "수업이 정말 좋았어요! 선생님도 친절하고 동작 설명도 자세해서 초보자도 따라하기 쉬웠습니다.",
                    rating = 4.5,
                    createdAt = "2025-11-04T12:34:00.000+00:00",
                    nickname = "요가러버",
                    userLevel = 3,
                    userProfile = null
                ),
                NewReview(
                    reviewId = "2",
                    userUuid = "uuid-002",
                    thumbnail = null,
                    content = "공간도 깔끔하고 분위기가 너무 좋아요. 다음에도 꼭 올 것 같아요!",
                    rating = 5.0,
                    createdAt = "2025-10-20T09:00:00.000+00:00",
                    nickname = "필라테스왕",
                    userLevel = 7,
                    userProfile = null
                ),
                NewReview(
                    reviewId = "3",
                    userUuid = "uuid-003",
                    thumbnail = null,
                    content = "처음 방문했는데 생각보다 훨씬 만족스러웠습니다. 강추!",
                    rating = 4.0,
                    createdAt = "2025-09-15T15:20:00.000+00:00",
                    nickname = "건강지킴이",
                    userLevel = 1,
                    userProfile = null
                )
            )
        )
    }
}
