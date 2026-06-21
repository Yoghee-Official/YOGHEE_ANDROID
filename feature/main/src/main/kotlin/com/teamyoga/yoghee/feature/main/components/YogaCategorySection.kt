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

@Composable
fun YogaCategorySection(
    title: String?,
    onCategoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                        painter = painterResource(R.drawable.bg_category_unique),
                        contentScale = ContentScale.Crop
                    )
                    .noRippleClickable(onCategoryClick)
            ) {
                Text(
                    text = stringResource(R.string.category_always_fresh),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = BLACK
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(R.string.category_unique_yoga),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = BLACK
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                YogaCategoryItem(
                    name = stringResource(R.string.category_relax),
                    bg = R.drawable.bg_category_relax,
                    onClick = onCategoryClick,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                YogaCategoryItem(
                    name = stringResource(R.string.category_flow),
                    bg = R.drawable.bg_category_flow,
                    onClick = onCategoryClick,
                    modifier = Modifier.weight(1f)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                YogaCategoryItem(
                    name = stringResource(R.string.category_power),
                    bg = R.drawable.bg_category_power,
                    onClick = onCategoryClick,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                YogaCategoryItem(
                    name = stringResource(R.string.category_traditional_yoga),
                    bg = R.drawable.bg_category_traditional,
                    onClick = onCategoryClick,
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
    YogaCategorySection(title = "요가 카테고리", onCategoryClick = {})
}
