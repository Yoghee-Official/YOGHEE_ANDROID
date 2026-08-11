package com.teamyoga.yoghee.feature.registerClass.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.SAND_BEIGE
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme
import com.teamyoga.yoghee.core.ui.util.noRippleClickable

enum class ImageSource { CAMERA, GALLERY }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageSourcePickerBottomSheet(
    onDismiss: () -> Unit,
    onSelect: (ImageSource) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 50.dp),
        ) {
            SheetItem(
                text = "카메라로 촬영하기",
                onClick = { onSelect(ImageSource.CAMERA) },
            )
            SheetItem(
                text = "갤러리에서 불러오기",
                onClick = { onSelect(ImageSource.GALLERY) },
            )
        }
    }
}

@Composable
private fun SheetItem(
    text: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(start = 28.dp)
            .noRippleClickable(onClick),
        contentAlignment = Alignment.CenterStart
    ) {
        YogheeText(
            text = text,
            color = BLACK,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "ImageSourcePickerBottomSheet")
@Composable
private fun ImageSourcePickerBottomSheetPreview() {
    YogheeTheme {
        // ModalBottomSheet은 Preview에서 완전히 표시되지 않을 수 있어 내부 Column만 미리보기
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SAND_BEIGE)
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            SheetItem(text = "카메라로 촬영", onClick = {})
            HorizontalDivider()
            SheetItem(text = "갤러리에서 선택", onClick = {})
        }
    }
}
