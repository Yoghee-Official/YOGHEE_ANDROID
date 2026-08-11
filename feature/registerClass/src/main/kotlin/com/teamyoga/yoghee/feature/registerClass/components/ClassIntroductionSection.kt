package com.teamyoga.yoghee.feature.registerClass.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme

private const val TITLE_MAX_LENGTH = 22

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
