package com.teamyoga.yoghee.feature.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamyoga.yoghee.core.domain.model.ClassDetail
import com.teamyoga.yoghee.core.ui.theme.WHITE
import com.teamyoga.yoghee.feature.detail.components.DetailImagePager
import com.teamyoga.yoghee.feature.detail.components.DetailInfoSection
import com.teamyoga.yoghee.feature.detail.components.DetailMasterBanner
import com.teamyoga.yoghee.feature.detail.components.DetailTopBar

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
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WHITE),
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
    }
}
