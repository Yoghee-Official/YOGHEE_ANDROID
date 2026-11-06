package com.teamyoga.yoghee.feature.detail

import com.teamyoga.yoghee.core.ui.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamyoga.yoghee.core.common.formatReviewCreatedAt
import com.teamyoga.yoghee.core.domain.model.ClassReview
import com.teamyoga.yoghee.core.ui.component.YogheeImage
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.component.StarScore
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.GRAY
import com.teamyoga.yoghee.core.ui.theme.Green_D6F695
import com.teamyoga.yoghee.core.ui.theme.LIGHT_GRAY
import com.teamyoga.yoghee.core.ui.theme.WHITE

@Composable
fun ReviewListScreen(
    id: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReviewListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var sortMenuExpanded by remember { mutableStateOf(false) }
    LaunchedEffect(id) { viewModel.load(id) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WHITE)
            .systemBarsPadding(),
    ) {
        when (val state = uiState) {
            ReviewListUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            is ReviewListUiState.Error -> Text(
                text = state.message,
                color = BLACK,
                modifier = Modifier.align(Alignment.Center),
            )
            is ReviewListUiState.Success -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    // 헤더
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(start = 24.dp, end = 47.dp, top = 22.dp, bottom = 14.dp),
                    ) {
                        Image(
                            painterResource(R.drawable.ic_back),
                            contentDescription = "뒤로가기",
                            modifier = Modifier.size(width = 23.dp, height = 20.dp)
                        )
                        YogheeText(
                            text = "리뷰 목록",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = BLACK,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f),
                        )
                    }

                    // 상단 총 별점 / 리뷰 수 영역
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painterResource(R.drawable.ic_stardrawing),
                            contentDescription = null,
                            modifier = Modifier
                                .padding()
                                .size(width = 106.dp, height = 98.dp)
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            YogheeText(
                                text = "%.1f".format(state.rating),
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = BLACK,
                                modifier = Modifier.padding(top = 24.dp)
                            )

                            YogheeText(
                                text = "총 리뷰 수 ${"%,d".format(state.totalCount)}건",
                                color = GRAY,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                        Image(
                            painterResource(R.drawable.ic_sunface),
                            contentDescription = null,
                            modifier = Modifier
                                .padding()
                                .size(width = 124.dp, height = 145.dp)
                        )
                    }

                    HorizontalDivider(color = LIGHT_GRAY, thickness = 1.dp, modifier = Modifier.padding(16.dp))

                    // 필터 영역 (최신순, 오래된 순, 점수 높은 순, 점수 낮은 순)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        REVIEW_SORT_LABELS.forEach { (sort, label) ->
                            val isSelected = state.sort == sort
                            YogheeText(
                                text = label,
                                color = BLACK,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .clip(RoundedCornerShape(32.dp))
                                    .background(if (isSelected) Green_D6F695 else LIGHT_GRAY)
                                    .clickable { viewModel.changeSort(sort) }
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                            )
                        }
                    }

                    // 리뷰 목록
                    if (state.reviews.isEmpty()) {
                        YogheeText(
                            text = "등록된 리뷰가 없습니다.",
                            color = GRAY,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 64.dp),
                        )
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(state.reviews, key = ClassReview::reviewId) { review ->
                                ReviewListItem(review = review)
                            }
                            if (state.hasNext) {
                                item(key = "load_more") {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { viewModel.loadNextPage() }
                                            .padding(vertical = 20.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        if (state.isLoadingNextPage) {
                                            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                                        } else {
                                            YogheeText(
                                                text = state.nextPageError ?: "리뷰 더보기",
                                                color = if (state.nextPageError == null) BLACK else GRAY,
                                                fontSize = 13.sp,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private val REVIEW_SORT_LABELS = linkedMapOf(
    "recent" to "최신순",
    "oldest" to "오래된순",
    "desc" to "평점 높은순",
    "asc" to "평점 낮은순",
)

@Composable
private fun ReviewListItem(review: ClassReview) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp, start = 20.dp, end = 20.dp)
            .border(width = 1.dp, color = LIGHT_GRAY, shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        // 리뷰 이미지 영역
        if (review.thumbnail.isNotBlank()) {
            YogheeImage(
                model = review.thumbnail,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
                    .aspectRatio(296f / 233f)
                    .clip(RoundedCornerShape(8.dp)),
            )
        }

        // 회원 프로필 영역
        Row(verticalAlignment = Alignment.CenterVertically) {
            YogheeImage(
                model = review.profileUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(33.dp)
                    .clip(CircleShape),
            )
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                YogheeText(text = review.nickname, color = BLACK, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                YogheeText(
                    text = "Lv.${review.userLevel}",
                    color = BLACK,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }

        // 날짜 및 별점 영역
        Row(modifier = Modifier.padding(vertical = 12.dp)) {
            YogheeText(
                text = formatReviewCreatedAt(review.createdAt),
                color = GRAY,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.weight(1f))
            StarScore(score = review.rating)
        }
        YogheeText(
            text = review.content,
            color = BLACK,
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
    HorizontalDivider(color = LIGHT_GRAY, thickness = 1.dp)
}

