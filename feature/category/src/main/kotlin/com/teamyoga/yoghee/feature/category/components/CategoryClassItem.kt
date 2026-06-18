package com.teamyoga.yoghee.feature.category.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.domain.model.CategoryClass
import com.teamyoga.yoghee.core.ui.component.RatingLabel
import com.teamyoga.yoghee.core.ui.component.YogheeImagePager
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.MIND_ORANGE

@Composable
fun CategoryClassItem(
    item: CategoryClass,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 24.dp),
    ) {
        YogheeImagePager(
            images = item.images,
            isFavorite = item.isFavorite == true,
            contentDescription = item.className,
        )
        Spacer(modifier = Modifier.height(12.dp))

        // 클래스 이름
        Text(
            text = item.className.orEmpty(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = BLACK,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 강사명
            Text(
                text = item.masterName.orEmpty(),
                fontSize = 12.sp,
                color = BLACK,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
            )
            Spacer(modifier = Modifier.width(8.dp))
            // 별점, 리뷰
            RatingLabel(
                rating = item.rating,
                review = item.review,
                fontSize = 12.sp,
                color = BLACK,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.weight(1f))

            // 할인율
//            Text(
//                modifier = Modifier.padding(end = 12.dp),
//                text = item.masterName.orEmpty(),
//                fontSize = 16.sp,
//                color = MIND_ORANGE,
//                fontWeight = FontWeight.Bold,
//                maxLines = 1,
//            )
            // 가격
            Text(
                text = (item.price ?: 0).toString() + "원",
                fontSize = 16.sp,
                color = BLACK,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
            )
        }


    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryClassItemPreview() {
    CategoryClassItem(
        item = CategoryClass(
            classId = "0fe4dfb5-ecac-4e53-8545-549e1c52c4cf",
            className = "봄맞이 힐링 요가 원데이 클래스봄맞이 힐링 요가 원데이 클래스봄맞이 힐링 요가 원데이 클래스",
            address = "경기 남양주시 다산동",
            images = listOf("a", "b", "c"),
            masterId = "1a48d9d4-eeb4-4781-b816-9e8c2b4b0ba7",
            masterName = "김의영",
            rating = 4.8,
            review = 128,
            price = 35000,
            favoriteCount = 10,
            isFavorite = false,
        ),
    )
}