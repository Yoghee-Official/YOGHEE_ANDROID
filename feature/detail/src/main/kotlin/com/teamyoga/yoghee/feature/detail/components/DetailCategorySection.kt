package com.teamyoga.yoghee.feature.detail.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.domain.model.ClassCategory
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.GRAY_D9D9D9
import com.teamyoga.yoghee.core.ui.theme.LIGHT_GRAY
import com.teamyoga.yoghee.core.ui.theme.WHITE

private const val MAX_CATEGORY_COUNT = 5
private val ICON_SIZE = 42.dp

/**
 * 4c 수련에 녹아있는 요소 (카테고리) 영역
 * - 헤더 + 아이콘 LazyRow
 * - 최대 5개 노출, 매핑되지 않는 code 는 placeholder 로 노출
 */
@Composable
fun DetailCategorySection(
    categories: List<ClassCategory>,
    modifier: Modifier = Modifier,
) {
    if (categories.isEmpty()) return
    val visible = categories.take(MAX_CATEGORY_COUNT)

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "아래 요소가 함께 녹아있습니다.",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = BLACK,
            modifier = Modifier.padding(top = 32.dp, start = 28.dp, end = 28.dp),
            textDecoration = TextDecoration.Underline
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 24.dp),
        ) {
            items(visible) { category ->
                CategoryItem(category = category)
            }
        }
    }
}

@Composable
private fun CategoryItem(
    category: ClassCategory,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(
                color = WHITE,
                shape = RoundedCornerShape(8.dp)
            )
            .border(width = 1.dp, color = LIGHT_GRAY, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val iconRes = categoryIconRes(category.code)
        if (iconRes != null) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = category.name,
                modifier = Modifier.size(ICON_SIZE),
            )
        } else {
            // 매핑되지 않은 code 는 placeholder 회색 원.
            Box(
                modifier = Modifier
                    .size(ICON_SIZE)
                    .clip(CircleShape)
                    .background(GRAY_D9D9D9),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = category.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = BLACK,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/**
 * category.code → drawable 매핑. 미등록 code 는 null 반환 → placeholder.
 */
@DrawableRes
private fun categoryIconRes(code: String): Int? = when (code) {
    "C_FLYING" -> R.drawable.ic_flying
    "C_SIVANANDA" -> R.drawable.ic_sivananda
    else -> null
}

@Preview(showBackground = true)
@Composable
private fun DetailCategorySectionPreview() {
    DetailCategorySection(
        categories = listOf(
            ClassCategory(code = "C_FLYING", name = "플라잉"),
            ClassCategory(code = "C_SIVANANDA", name = "시바난다"),
            ClassCategory(code = "C_VINYASA", name = "빈야사"),
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun DetailCategorySectionOverflowPreview() {
    DetailCategorySection(
        categories = listOf(
            ClassCategory(code = "C_FLYING", name = "플라잉"),
            ClassCategory(code = "C_SIVANANDA", name = "시바난다"),
            ClassCategory(code = "C_A", name = "카테고리A"),
            ClassCategory(code = "C_B", name = "카테고리B"),
            ClassCategory(code = "C_C", name = "카테고리C"),
            ClassCategory(code = "C_D", name = "노출안됨"),
        ),
    )
}
