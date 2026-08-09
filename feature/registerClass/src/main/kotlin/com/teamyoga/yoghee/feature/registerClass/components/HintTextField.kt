package com.teamyoga.yoghee.feature.registerClass.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.GRAY
import com.teamyoga.yoghee.core.ui.theme.LIGHT_GRAY
import com.teamyoga.yoghee.core.ui.theme.WHITE
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme

const val CONTENT_MAX_LENGTH = 3000

@Composable
fun HintTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint1: String,
    hint2: String,
    maxLength: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(WHITE)
            .border(width = 1.dp, color = LIGHT_GRAY, shape = RoundedCornerShape(8.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (value.isEmpty()) {
                Column {
                    YogheeText(
                        text = hint1,
                        color = GRAY,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    if (hint2.isNotEmpty()) {
                        YogheeText(
                            text = hint2,
                            color = GRAY,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                }
            }
            BasicTextField(
                value = value,
                onValueChange = { if (it.length <= maxLength) onValueChange(it) },
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = TextStyle(
                    color = BLACK,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                ),
                maxLines = 4
            )
        }
        YogheeText(
            text = "${value.length} / $maxLength",
            color = GRAY,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.Start).padding(top = 12.dp),
        )
    }
}

@Preview(showBackground = true, name = "HintTextField")
@Composable
private fun HintTextFieldPreview() {
    var value by remember { mutableStateOf("") }
    YogheeTheme {
        HintTextField(
            value = value,
            onValueChange = { value = it },
            hint1 = "내용",
            hint2 = "수련 관련 내용을 작성해주세요.",
            maxLength = CONTENT_MAX_LENGTH,
            modifier = Modifier.padding(24.dp),
        )
    }
}
