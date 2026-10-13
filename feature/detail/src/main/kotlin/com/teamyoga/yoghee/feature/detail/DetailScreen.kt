package com.teamyoga.yoghee.feature.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamyoga.yoghee.core.domain.model.ClassDetail
import com.teamyoga.yoghee.core.domain.model.ClassReview
import com.teamyoga.yoghee.core.domain.model.NewReview
import com.teamyoga.yoghee.core.ui.component.NewReviewSection
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.GHEE_YELLO
import com.teamyoga.yoghee.core.ui.theme.GRAY
import com.teamyoga.yoghee.core.ui.theme.Green_D6F695
import com.teamyoga.yoghee.core.ui.theme.MIND_ORANGE
import com.teamyoga.yoghee.core.ui.theme.WHITE
import com.teamyoga.yoghee.core.ui.util.noRippleClickable
import com.teamyoga.yoghee.feature.detail.components.DetailAmenitySection
import com.teamyoga.yoghee.feature.detail.components.DetailCategorySection
import com.teamyoga.yoghee.feature.detail.components.DetailImagePager
import com.teamyoga.yoghee.feature.detail.components.DetailInfoSection
import com.teamyoga.yoghee.feature.detail.components.DetailLocationMap
import com.teamyoga.yoghee.feature.detail.components.DetailMasterBanner
import com.teamyoga.yoghee.feature.detail.components.DetailTimeTable
import com.teamyoga.yoghee.feature.detail.components.DetailTopBar
import com.teamyoga.yoghee.feature.detail.components.RoundBorderTitle

@Composable
fun DetailScreen(
    id: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // classId 가 바뀌면 재로딩
    LaunchedEffect(id) {
        viewModel.load(id)
    }

    when (val state = uiState) {
        DetailUiState.Loading -> LoadingContent(modifier)
        is DetailUiState.Success -> SuccessContent(
            classDetail = state.classDetail,
            isBookmarked = state.isBookmarked,
            onBack = onBack,
            onToggleBookmark = viewModel::toggleBookmark,
            // TODO: 대표 이미지 클릭 시 이미지 팝업 화면 이동 (후속)
            onImageClick = {},
            // TODO: 리뷰 평점 클릭 시 리뷰 영역으로 앵커링 이동 (후속: 리뷰 섹션 구현 후)
            onRatingClick = {},
            modifier = modifier,
        )
        is DetailUiState.Error -> ErrorContent(
            message = state.message,
            onBack = onBack,
            modifier = modifier,
        )
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WHITE)
            .systemBarsPadding(),
    ) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WHITE)
            .systemBarsPadding(),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp),
        ) {
            Text(text = message)
        }
        DetailTopBar(
            isBookmarked = false,
            onBack = onBack,
            onBookmarkClick = {},
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}

private val BOTTOM_BAR_HEIGHT = 92.dp

@Composable
private fun SuccessContent(
    classDetail: ClassDetail,
    isBookmarked: Boolean,
    onBack: () -> Unit,
    onToggleBookmark: () -> Unit,
    onImageClick: (page: Int) -> Unit,
    onRatingClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier
        .fillMaxSize()
        .background(WHITE)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            // BottomBar 에 가리지 않도록 하단 패딩 추가.
            contentPadding = PaddingValues(bottom = BOTTOM_BAR_HEIGHT + 93.dp),
        ) {
        // Section 1 + Section 2: 이미지 위에 TopBar 오버레이. 스크롤 시 함께 이동.
        item {
            Box {
                DetailImagePager(
                    images = classDetail.images,
                    onImageClick = onImageClick,
                )
                DetailTopBar(
                    isBookmarked = isBookmarked,
                    onBack = onBack,
                    onBookmarkClick = onToggleBookmark,
                    modifier = Modifier.align(Alignment.TopCenter),
                )
            }
        }
        // Section 3: 정보 영역 (이미지 위로 오버랩되어 올라옴)
        item {
            DetailInfoSection(
                className = classDetail.name,
                rating = classDetail.rating,
                reviewCount = classDetail.reviewCount,
                center = classDetail.center,
                features = classDetail.features,
                onRatingClick = onRatingClick,
                modifier = Modifier
                    .offset(y = (-20).dp)
                    .background(
                        color = WHITE,
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    ),
            )
        }
        // Section 4a + 4aa: 대표 강사 배너 (플립)
        classDetail.masterInfo?.let { masterInfo ->
            item {
                DetailMasterBanner(masterInfo = masterInfo)
            }
        }

        // Section 4b: 수련 설명
        item {
            RoundBorderTitle(
                text = "수련 설명",
                modifier = Modifier.padding(top = 32.dp, start = 16.dp, end = 16.dp),
            )
            YogheeText(
                text = classDetail.description,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = BLACK,
                modifier = Modifier
                    .padding(top = 16.dp, start = 24.dp, end = 24.dp)
            )
        }

        // Section 4c: 수련에 녹아있는 요소 (카테고리)
        item {
            DetailCategorySection(categories = classDetail.categories)
            // todo:: 수련 방식
            // todo:: 수련 대상
        }

        item {
            DetailAmenitySection(title = "수련원 보유 물품", amenities = classDetail.center?.amenities.orEmpty(), 21)
            DetailAmenitySection(title = "편의시설", amenities = classDetail.center?.facilities.orEmpty(), 10)
            DetailTimeTable(title = "수련 시간표", schedules = classDetail.schedules)
            DetailLocationMap(title = "위치", center = classDetail.center)
        }

        if (classDetail.recentReviews.isNotEmpty()) {
            item {
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = GRAY,
                    modifier = Modifier.padding(top = 32.dp, bottom = 32.dp, start = 16.dp, end = 16.dp),
                )
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RoundBorderTitle(text = "리뷰 ★${classDetail.rating}")
                    Spacer(modifier = Modifier.weight(1f))
                    YogheeText(
                        // 99,999 초과면 "99,999+개" 로 표시 (디자인상 5자리 캡).
                        text = if (classDetail.reviewCount > 99_999) "99,999+개"
                        else "${"%,d".format(classDetail.reviewCount)}개",
                        fontSize = 12.sp,
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Medium,
                        color = BLACK,
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                NewReviewSection(reviews = classDetail.recentReviews.map { it.toNewReview() })
                Spacer(modifier = Modifier.height(16.dp))
                // 리뷰 전체보기
                YogheeText(
                    text = "리뷰 전체보기",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = BLACK,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .background(color = GHEE_YELLO, shape = RoundedCornerShape(8.dp))
                        .padding(10.dp)
                )
            }
        }
        }
        DetailBottomBar(
            price = classDetail.price,
            // TODO: 문의하기 액션 연결
            onInquiry = {},
            // TODO: 예약하기 액션 연결
            onReserve = {},
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun DetailBottomBar(
    price: Int,
    onInquiry: () -> Unit,
    onReserve: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(BOTTOM_BAR_HEIGHT)
            .background(color = Green_D6F695)
            .padding(top = 25.dp, bottom = 48.dp, start = 26.dp, end = 17.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        YogheeText(
            text = "1회 금액",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = BLACK,
        )
        YogheeText(
            text = "${"%,d".format(price)}원",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = BLACK,
            modifier = Modifier.padding(start = 4.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        YogheeText(
            text = "문의하기",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = BLACK,
            modifier = Modifier
                .noRippleClickable(onInquiry)
        )
        VerticalDivider(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 28.dp),
            thickness = 1.dp,
            color = BLACK,
        )
        YogheeText(
            text = "예약하기",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MIND_ORANGE,
            modifier = Modifier
                .noRippleClickable(onReserve),
        )
    }
}

/**
 * ClassReview(모든 필드 non-null) 를 공통 UI 컴포넌트가 요구하는 NewReview(nullable) 로 변환.
 * 두 모델이 사실상 같은 데이터를 담지만 nullability 가 달라 매핑이 필요.
 */
private fun ClassReview.toNewReview(): NewReview = NewReview(
    reviewId = reviewId,
    userUuid = userUuid,
    thumbnail = thumbnail,
    content = content,
    rating = rating,
    createdAt = createdAt,
    nickname = nickname,
    userLevel = userLevel,
    profileUrl = profileUrl,
)
