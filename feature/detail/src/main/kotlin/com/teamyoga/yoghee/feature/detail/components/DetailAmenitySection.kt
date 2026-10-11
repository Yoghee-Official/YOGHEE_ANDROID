package com.teamyoga.yoghee.feature.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.GRAY

private const val COLUMN_COUNT = 3

/**
 * 수련원 보유 물품 영역: amenities 를 3열 균등 간격으로 노출.
 * 최대 21개(3x7) 까지 노출한다.
 */
@Composable
fun DetailAmenitySection(
    title: String,
    amenities: List<String>,
    maxCount: Int,
    modifier: Modifier = Modifier,
) {
    if (amenities.isEmpty()) return
    val visible = amenities.take(maxCount)
    val rows = visible.chunked(COLUMN_COUNT)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(11.dp),
    ) {
        HorizontalDivider(thickness = 0.5.dp, color = GRAY, modifier = Modifier.padding(top = 32.dp, bottom = 21.dp))
        RoundBorderTitle(text = title, modifier = Modifier.padding(bottom = 5.dp))
        rows.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                repeat(COLUMN_COUNT) { index ->
                    val text = row.getOrNull(index)
                    Box(
                        modifier = Modifier.weight(1f),
                    ) {
                        if (text != null) {
                            Text(
                                text = "•  ${text}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = BLACK,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
        }
    }
}
