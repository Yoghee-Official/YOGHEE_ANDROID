package com.teamyoga.yoghee.feature.detail.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.theme.BLACK

/**
 * 둥근 테두리가 있는 제목 라벨 (예: 수련 설명, 수련 방식).
 * - 검은색 1dp 테두리 + 28dp 라운드 + 굵은 14sp 텍스트
 * - 외부 위치/여백은 호출부에서 modifier 로 제어한다.
 */
@Composable
fun RoundBorderTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = BLACK,
        modifier = modifier
            .border(
                width = 1.dp,
                color = BLACK,
                shape = RoundedCornerShape(28.dp),
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun RoundBorderTitlePreview() {
    RoundBorderTitle(
        text = "수련 설명",
        modifier = Modifier.padding(16.dp),
    )
}
