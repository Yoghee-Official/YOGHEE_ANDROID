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

private const val TITLE_MAX_LENGTH = 30
private const val CONTENT_MAX_LENGTH = 500

@Composable
fun ClassIntroductionModule(
    modifier: Modifier = Modifier,
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 20.dp),
    ) {
        YogheeText(
            text = "수련에 대해 알려주세요.",
            color = BLACK,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp)
        )
        YogheeText(
            text = "[마이페이지]->[개선 수련 목록]에서 수정할 수 있습니다.",
            color = GRAY,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 4.dp, start = 8.dp)
        )
        HintTextField(
            value = title,
            onValueChange = { if (it.length <= TITLE_MAX_LENGTH) title = it },
            hint = "대표 제목 (상세페이지 최상단에 노출돼요!)\n수련 테마를 한줄로 표현해주세요.",
            maxLength = TITLE_MAX_LENGTH,
            modifier = Modifier.padding(top = 10.dp)
        )
        HintTextField(
            value = content,
            onValueChange = { if (it.length <= CONTENT_MAX_LENGTH) content = it },
            hint = "내용\n수련 관련 내용을 작성해주세요.",
            maxLength = CONTENT_MAX_LENGTH,
            modifier = Modifier.padding(top = 14.dp)
        )
    }
}

@Composable
private fun HintTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    maxLength: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(WHITE).border(width = 1.dp, color = LIGHT_GRAY)
            .padding(top = 8.dp, bottom = 20.dp, start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (value.isEmpty()) {
                YogheeText(
                    text = hint,
                    color = GRAY,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 36.sp,
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    color = BLACK,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                ),
            )
        }
        YogheeText(
            text = "${value.length} / $maxLength",
            color = GRAY,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.Start),
        )
    }
}

@Preview(showBackground = true, name = "ClassIntroductionModule")
@Composable
private fun ClassIntroductionModulePreview() {
    YogheeTheme {
        ClassIntroductionModule(
            modifier = Modifier.padding(24.dp),
        )
    }
}
