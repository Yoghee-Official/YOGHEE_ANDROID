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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.LIGHT_GRAY
import com.teamyoga.yoghee.core.ui.theme.MIND_ORANGE
import com.teamyoga.yoghee.core.ui.theme.WHITE
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme
import com.teamyoga.yoghee.core.ui.util.noRippleClickable

@Composable
fun AddressFieldStatic(
    label: String,
    value: String
) {
    FieldBox(label = label, required = true) {
        ValueText(
            text = value
        )
    }
}

@Composable
fun AddressFieldButton(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    FieldBox(label = label, required = true, onClick = onClick) {
        ValueText(
            text = value
        )
    }
}

@Composable
fun AddressFieldReadOnly(
    label: String,
    value: String
) {
    FieldBox(label = label, required = true) {
        ValueText(
            text = value,
        )
    }
}

@Composable
fun AddressFieldEditable(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    required: Boolean = false,
) {
    FieldBox(label = label, required = required) {
        Box(modifier = Modifier.fillMaxWidth()) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    color = BLACK,
                    fontSize = 14.sp,
                ),
                singleLine = true,
            )
        }
    }
}

@Composable
private fun FieldBox(
    label: String,
    required: Boolean = false,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val baseModifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp))
        .background(WHITE)
        .border(width = 1.dp, color = LIGHT_GRAY, shape = RoundedCornerShape(8.dp))
    val clickableModifier = if (onClick != null) baseModifier.noRippleClickable(onClick) else baseModifier
    Column(
        modifier = clickableModifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        YogheeText(
            text = buildAnnotatedString {
                append(label)
                if (required) {
                    withStyle(style = SpanStyle(color = MIND_ORANGE)) {
                        append(" *")
                    }
                }
            },
            fontSize = 10.sp,
        )
        content()
    }
}

@Composable
private fun ValueText(
    text: String
) {
    YogheeText(
        text = text,
        color = BLACK,
        fontSize = 14.sp
    )
}

@Preview(showBackground = true, name = "AddressField Variants")
@Composable
private fun AddressFieldPreview() {
    YogheeTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AddressFieldStatic(label = "국가/지역", value = "대한민국")
        }
    }
}
