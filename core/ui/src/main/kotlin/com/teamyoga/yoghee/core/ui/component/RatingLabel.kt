package com.teamyoga.yoghee.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.theme.BLACK

/**
 * 별점 + 리뷰 수 라벨. ★ 4.8 (128) 형태
 */
@Composable
fun RatingLabel(
    rating: Double?,
    review: Int?,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 10.sp,
    color: Color = BLACK,
) {
    // 리뷰 수는 99,999를 넘으면 "99,999+"로 표기한다.
    val reviewCount = review?.let {
        if (it > 99999) "99,999+" else "%,d".format(it)
    } ?: "0"

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            modifier = Modifier.size(12.dp),
        )
        Text(
            text = "${rating ?: 0} ($reviewCount)",
            fontSize = fontSize,
            color = color,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RatingLabelPreview() {
    RatingLabel(rating = 4.8, review = 128)
}

@Preview(showBackground = true)
@Composable
private fun RatingLabelEmptyPreview() {
    RatingLabel(rating = null, review = null)
}

@Preview(showBackground = true)
@Composable
private fun RatingLabelLargeCountPreview() {
    RatingLabel(rating = 4.9, review = 123456)
}
