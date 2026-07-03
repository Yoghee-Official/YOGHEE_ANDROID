package com.teamyoga.yoghee.feature.contentFeed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.teamyoga.yoghee.core.domain.model.FeedContent
import com.teamyoga.yoghee.core.domain.model.FeedItem
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.component.YogheeHeader
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.FLOW_BLUE
import com.teamyoga.yoghee.core.ui.theme.SAND_BEIGE
import com.teamyoga.yoghee.core.ui.R.drawable.ic_arrow_right
import com.teamyoga.yoghee.core.ui.R.drawable.ic_arrow_left
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme
import com.teamyoga.yoghee.core.ui.util.noRippleClickable

@Composable
fun ContentFeedScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ContentFeedViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ContentFeedScreen(uiState = uiState, onBack = onBack, modifier = modifier)
}

@Composable
internal fun ContentFeedScreen(
    uiState: ContentFeedUiState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0f to FLOW_BLUE,
                        0.5f to SAND_BEIGE,
                        1f to SAND_BEIGE,
                    ),
                ),
            ),
    ) {
        YogheeHeader(
            title = stringResource(R.string.content_feed_title),
            onBack = onBack,
        )

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (uiState) {
                ContentFeedUiState.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) { CircularProgressIndicator() }

                is ContentFeedUiState.Error -> EmptyContent()

                is ContentFeedUiState.Success ->
                    if (uiState.feed.items.isEmpty()) {
                        EmptyContent()
                    } else {
                        FeedList(
                            weekLabel = uiState.feed.weekLabel,
                            items = uiState.feed.items,
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                        )
                    }
            }
        }
    }
}

@Composable
private fun EmptyContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        YogheeText(
            text = stringResource(R.string.content_feed_empty_title),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = BLACK,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(12.dp))
        YogheeText(
            text = stringResource(R.string.content_feed_empty_subtitle),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = BLACK,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun FeedList(
    weekLabel: String,
    items: List<FeedItem>,
    modifier: Modifier = Modifier,
) {
    if (items.isEmpty()) return

    var index by rememberSaveable { mutableIntStateOf(0) }
    val safeIndex = index.coerceIn(0, items.lastIndex)
    val current = items[safeIndex]
    val canGoPrev = safeIndex > 0
    val canGoNext = safeIndex < items.lastIndex

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = current.imageUrl,
                contentDescription = current.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(343f / 440f)
                    .clip(RoundedCornerShape(8.dp)),
            )
            if (canGoPrev) {
                Image(
                    painter = painterResource(ic_arrow_left),
                    contentDescription = "이전 콘텐츠",
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(31.dp)
                        .align(Alignment.CenterStart)
                        .noRippleClickable(onClick = { index = safeIndex - 1 })
                )
            }
            if (canGoNext) {
                Image(
                    painter = painterResource(ic_arrow_right),
                    contentDescription = "다음 콘텐츠",
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(31.dp)
                        .align(Alignment.CenterEnd)
                        .noRippleClickable(onClick = { index = safeIndex + 1 })
                )
            }
        }
        Row() {
            YogheeText(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = BLACK,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                text = weekLabel,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = BLACK,
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column() {
                YogheeText(
                    text = current.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp, bottom = 12.dp),
                    color = BLACK,
                )
                YogheeText(
                    text = current.description,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = BLACK,
                )
            }
        }
    }
}

private val previewFeed = FeedContent(
    weekLabel = "4월 3주차",
    items = listOf(
        FeedItem(
            title = "마음 안정 요가",
            description = "하루를 마무리하는 명상 요가입니다.\n하루를 마무리하는 명상 요가입니다.하루를 마무리하는 명상 요가입니다.",
            imageUrl = "",
        ),
        FeedItem(
            title = "코어 강화",
            description = "복부와 코어 근육을 단련합니다.",
            imageUrl = "",
        ),
    ),
)

@Preview(showBackground = true, name = "Success")
@Composable
private fun ContentFeedScreenSuccessPreview() {
    YogheeTheme {
        ContentFeedScreen(uiState = ContentFeedUiState.Success(previewFeed), onBack = {})
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun ContentFeedScreenLoadingPreview() {
    YogheeTheme {
        ContentFeedScreen(uiState = ContentFeedUiState.Loading, onBack = {})
    }
}
