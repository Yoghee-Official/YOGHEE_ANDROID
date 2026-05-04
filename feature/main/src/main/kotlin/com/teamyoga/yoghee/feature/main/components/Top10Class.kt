package com.teamyoga.yoghee.feature.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.paint
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.teamyoga.yoghee.core.domain.model.InterestedClass
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.component.YogheeImage
import com.teamyoga.yoghee.core.ui.theme.BLACK

@Composable
fun Top10Class(
    title: String?,
    classData: List<InterestedClass>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        if (!title.isNullOrEmpty()) {
            Title(title = title)
            Spacer(modifier = Modifier.height(12.dp))
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            itemsIndexed(classData) { index, item ->
                Top10ClassItem(index + 1, item)
            }
        }
    }
}

@Composable
fun Top10ClassItem(
    index: Int,
    classData: InterestedClass
) {
    Column(
        modifier = Modifier.width(145.dp)
    ) {
        Box(modifier = Modifier.size(145.dp)) {
            YogheeImage(
                model = classData.thumbnail,
                contentDescription = classData.className,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(145.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            RankFlag(
                rank = index.toString(),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.ic_flag_unselected),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .width(16.dp)
                    .height(19.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = classData.masterName.orEmpty(),
            fontSize = 10.sp,
            maxLines = 1,
            color = BLACK
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = classData.className.orEmpty(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            color = BLACK
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            val reviewCount = classData.review?.let {
                if (it > 99999) "99,999+" else "%,d".format(it)
            } ?: 0
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = "${classData.rating ?: 0} (${reviewCount})",
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun RankFlag(rank: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(20.dp)
            .paint(
                painter = painterResource(id = R.drawable.ic_rank),
                contentScale = ContentScale.Fit
            ),
        contentAlignment = Alignment.Center // 내부 구성 요소를 중앙 정렬
    ) {
        Text(
            text = rank,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Top10ClassPreview() {
    Top10Class(
        title = "관심 클래스",
        classData = listOf(
            InterestedClass(
                classId = "1",
                className = "아침 요가 클래스",
                masterId = "m1",
                masterName = "김요가",
                review = 128,
                price = 25000,
                rating = 4.8,
                isFavorite = false,
                thumbnail = null
            ),
            InterestedClass(
                classId = "2",
                className = "저녁 명상 요가",
                masterId = "m2",
                masterName = "이명상",
                review = 64,
                price = 30000,
                rating = 4.5,
                isFavorite = true,
                thumbnail = null
            ),
            InterestedClass(
                classId = "3",
                className = "주말 빈야사",
                masterId = "m3",
                masterName = "박빈야사",
                review = 256,
                price = 35000,
                rating = 4.9,
                isFavorite = false,
                thumbnail = null
            )
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun Top10ClassItemPreview() {
    InterestedClassItem(
        classData = InterestedClass(
            classId = "1",
            className = "아침 요가 클래스",
            masterId = "m1",
            masterName = "김요가",
            review = 128,
            price = 25000,
            rating = 4.8,
            isFavorite = false,
            thumbnail = null
        )
    )
}
