package com.teamyoga.yoghee.feature.detail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.theme.SAND_BEIGE
import com.teamyoga.yoghee.core.ui.theme.SAND_BEIGE_ALPHA50

@Composable
fun DetailTopBar(
    isBookmarked: Boolean,
    onBack: () -> Unit,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bookmarkIconRes = if (isBookmarked) R.drawable.ic_flag_selected else R.drawable.ic_flag_unselected

    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 32.dp, vertical = 22.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "뒤로가기",
            modifier = Modifier
                .padding(top = 5.dp)
                .width(23.dp)
                .height(20.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onBack,
                ),
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(SAND_BEIGE_ALPHA50)
            ) {
                Image(
                    painter = painterResource(id = bookmarkIconRes),
                    contentDescription = "북마크",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp, horizontal = 9.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onBookmarkClick,
                        ),
                )
            }

            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(SAND_BEIGE_ALPHA50)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_share),
                    contentDescription = "북마크",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { },
                        ),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailTopBarUnselectedPreview() {
    DetailTopBar(
        isBookmarked = false,
        onBack = {},
        onBookmarkClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun DetailTopBarSelectedPreview() {
    DetailTopBar(
        isBookmarked = true,
        onBack = {},
        onBookmarkClick = {},
    )
}
