package com.teamyoga.yoghee.feature.registerClass

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.component.YogheeHeader
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.component.YogheeToggle
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.LIGHT_GRAY
import com.teamyoga.yoghee.core.ui.theme.SAND_BEIGE
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme

@Composable
internal fun Step6Content(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(modifier = modifier.fillMaxSize()) {
        YogheeHeader(
            title = stringResource(R.string.one_day_class_register_step6_title),
            onBack = onBack,
            subTitle = stringResource(R.string.inquire),
            onSubTitleClick = {},
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
        ) {
            EnterPrice()
            HorizontalDivider(thickness = 1.dp, color = LIGHT_GRAY, modifier = Modifier.padding(horizontal = 8.dp, vertical = 20.dp))
            DiscountArea()
        }
    }
}

@Composable
fun EnterPrice() {
    var priceRaw by rememberSaveable { mutableStateOf("") }
    // 1회 수업 가격
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 38.dp, start = 8.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        YogheeText(
            text = "1회수업",
            color = BLACK,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        BasicTextField(
            value = priceRaw,
            onValueChange = { input ->
                priceRaw = input.filter { it.isDigit() }.trimStart('0')
            },
            modifier = Modifier.weight(1f),
            textStyle = TextStyle(
                color = BLACK,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = ThousandsSeparatorTransformation,
            singleLine = true,
        )
        YogheeText(
            text = "원",
            color = BLACK,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun DiscountArea() {
    var discountEnabled by rememberSaveable { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        YogheeText(
            text = "할인 적용",
            color = BLACK,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.weight(1f))
        YogheeToggle(
            checked = discountEnabled,
            onCheckedChange = { discountEnabled = it },
        )
    }
}

// 숫자 문자열에 천 단위 콤마를 추가하는 VisualTransformation.
private object ThousandsSeparatorTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text
        val formatted = if (digits.isEmpty()) {
            ""
        } else {
            digits.reversed().chunked(3).joinToString(",").reversed()
        }

        // 커서 위치를 원본 문자열 화면 표시 문자열 사이에서 정확히 맞춰주는 매핑
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (digits.isEmpty()) return 0
                val totalCommas = (digits.length - 1) / 3
                val digitsToRight = digits.length - offset
                val commasToRight = if (digitsToRight > 0) (digitsToRight - 1) / 3 else 0
                return offset + (totalCommas - commasToRight)
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 0) return 0
                if (offset >= formatted.length) return digits.length
                return formatted.take(offset).count { it != ',' }
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}

@Preview(showBackground = true, name = "Step6Content")
@Composable
private fun Step6ContentPreview() {
    YogheeTheme {
        Box(modifier = Modifier.background(SAND_BEIGE)) {
            Step6Content(onBack = {})
        }
    }
}
