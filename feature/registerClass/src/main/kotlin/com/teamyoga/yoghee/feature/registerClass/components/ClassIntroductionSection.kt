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

private const val TITLE_MAX_LENGTH = 22
private const val CONTENT_MAX_LENGTH = 3000

@Composable
fun ClassIntroductionSection(
    title: String,
    content: String,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
    ) {
        RegisterSectionTitle(
            title = "수련에 대해 알려주세요.",
            subTitle = "[마이페이지]->[개선 수련 목록]에서 수정할 수 있습니다.",
        )
        HintTextField(
            value = title,
            onValueChange = onTitleChange,
            hint1 = "대표 제목 (상세페이지 최상단에 노출돼요!)",
            hint2 = "수련 테마를 한줄로 표현해주세요.",
            maxLength = TITLE_MAX_LENGTH,
            modifier = Modifier.padding(top = 10.dp)
        )
        HintTextField(
            value = content,
            onValueChange = onContentChange,
            hint1 = "내용",
            hint2 = "수련 관련 내용을 작성해주세요.",
            maxLength = CONTENT_MAX_LENGTH,
            modifier = Modifier.padding(top = 14.dp)
        )
    }
}

@Composable
private fun HintTextField(
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
                    YogheeText(
                        text = hint2,
                        color = GRAY,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 12.dp)
                    )
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

@Preview(showBackground = true, name = "ClassIntroductionSection")
@Composable
private fun ClassIntroductionSectionPreview() {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    YogheeTheme {
        ClassIntroductionSection(
            title = title,
            content = content,
            onTitleChange = { title = it },
            onContentChange = { content = it },
            modifier = Modifier.padding(24.dp),
        )
    }
}
