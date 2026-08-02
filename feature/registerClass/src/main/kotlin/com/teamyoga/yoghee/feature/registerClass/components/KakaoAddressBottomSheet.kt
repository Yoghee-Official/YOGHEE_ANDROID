package com.teamyoga.yoghee.feature.registerClass.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teamyoga.yoghee.core.ui.theme.WHITE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KakaoAddressBottomSheet(
    onDismiss: () -> Unit,
    onAddressSelected: (KakaoAddressResult) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = WHITE,
        modifier = modifier,
    ) {
        KakaoAddressWebView(
            onAddressSelected = { result ->
                onAddressSelected(result)
                onDismiss()
            },
            onClose = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .height(560.dp),
        )
    }
}
