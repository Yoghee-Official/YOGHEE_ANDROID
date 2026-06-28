package com.teamyoga.yoghee.feature.main.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.util.noRippleClickable

private data class YogaTile(
    val subtitleRes: Int,
    val titleRes: Int,
    @param:DrawableRes val bg: Int,
)

private data class YogaSmallTile(
    val titleRes: Int,
    @param:DrawableRes val bg: Int,
)

@Composable
fun YogaCategorySection(
    title: String?,
    showLocations: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val featured = if (showLocations) {
        YogaTile(
            subtitleRes = R.string.location_subtitle,
            titleRes = R.string.location_seoul,
            bg = R.drawable.bg_category_unique,
        )
    } else {
        YogaTile(
            subtitleRes = R.string.category_always_fresh,
            titleRes = R.string.category_unique_yoga,
            bg = R.drawable.bg_category_unique,
        )
    }
    val smallTiles = if (showLocations) {
        listOf(
            YogaSmallTile(R.string.location_gyeonggi, R.drawable.bg_category_relax),
            YogaSmallTile(R.string.location_gyeongsang, R.drawable.bg_category_flow),
            YogaSmallTile(R.string.location_jeolla, R.drawable.bg_category_power),
            YogaSmallTile(R.string.location_chungcheong, R.drawable.bg_category_traditional),
        )
    } else {
        listOf(
            YogaSmallTile(R.string.category_relax, R.drawable.bg_category_relax),
            YogaSmallTile(R.string.category_flow, R.drawable.bg_category_flow),
            YogaSmallTile(R.string.category_power, R.drawable.bg_category_power),
            YogaSmallTile(R.string.category_traditional_yoga, R.drawable.bg_category_traditional),
        )
    }

    Column(modifier = modifier) {
        if (!title.isNullOrEmpty()) {
            Title(title = title)
            Spacer(modifier = Modifier.height(8.dp))
            SubTitle(subTitle = "더욱 다양한 카테고리는 탐색탭을 활용해보세요!", modifier = Modifier.padding(bottom = 16.dp))
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(horizontal = 16.dp).height(150.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .paint(
                        painter = painterResource(featured.bg),
                        contentScale = ContentScale.Crop
                    )
                    .noRippleClickable(onClick)
            ) {
                Text(
                    text = stringResource(featured.subtitleRes),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = BLACK
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(featured.titleRes),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = BLACK
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                YogaCategoryItem(
                    name = stringResource(smallTiles[0].titleRes),
                    bg = smallTiles[0].bg,
                    onClick = onClick,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                YogaCategoryItem(
                    name = stringResource(smallTiles[1].titleRes),
                    bg = smallTiles[1].bg,
                    onClick = onClick,
                    modifier = Modifier.weight(1f)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                YogaCategoryItem(
                    name = stringResource(smallTiles[2].titleRes),
                    bg = smallTiles[2].bg,
                    onClick = onClick,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                YogaCategoryItem(
                    name = stringResource(smallTiles[3].titleRes),
                    bg = smallTiles[3].bg,
                    onClick = onClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun YogaCategoryItem(
    name: String,
    @DrawableRes bg: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .paint(
                painter = painterResource(bg)
            )
            .noRippleClickable(onClick)
    ) {
        Text(
            text = name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = BLACK
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun YogaCategorySectionPreview() {
    YogaCategorySection(title = "요가 카테고리", showLocations = false, onClick = {})
}

@Preview(showBackground = true)
@Composable
private fun YogaCategorySectionLocationsPreview() {
    YogaCategorySection(title = "지역", showLocations = true, onClick = {})
}
