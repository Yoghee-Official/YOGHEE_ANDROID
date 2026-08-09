package com.teamyoga.yoghee.feature.registerClass

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.component.YogheeHeader
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.SAND_BEIGE
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme
import com.teamyoga.yoghee.core.ui.util.noRippleClickable
import com.teamyoga.yoghee.feature.registerClass.components.AddressFieldButton
import com.teamyoga.yoghee.feature.registerClass.components.AddressFieldEditable
import com.teamyoga.yoghee.feature.registerClass.components.AddressFieldReadOnly
import com.teamyoga.yoghee.feature.registerClass.components.AddressFieldStatic
import com.teamyoga.yoghee.feature.registerClass.components.CONTENT_MAX_LENGTH
import com.teamyoga.yoghee.feature.registerClass.components.HintTextField
import com.teamyoga.yoghee.feature.registerClass.components.KakaoAddressBottomSheet
import com.teamyoga.yoghee.feature.registerClass.components.KakaoAddressResult
import com.teamyoga.yoghee.feature.registerClass.components.RegisterSectionTitle

@Composable
fun RegisterCenterScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var address by remember { mutableStateOf(CenterForm()) }
    var showAddressSheet by remember { mutableStateOf(false) }

    RegisterCenterContent(
        address = address,
        onAddressChange = { address = it },
        onSearchAddressClick = { showAddressSheet = true },
        onRegisterClick = { /* TODO: 등록 API 연동 */ onBack() },
        onBack = onBack,
        modifier = modifier,
    )

    if (showAddressSheet) {
        KakaoAddressBottomSheet(
            onDismiss = { showAddressSheet = false },
            onAddressSelected = { result ->
                address = address.applyKakaoResult(result)
            },
        )
    }
}

@Composable
private fun RegisterCenterContent(
    address: CenterForm,
    onAddressChange: (CenterForm) -> Unit,
    onSearchAddressClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SAND_BEIGE)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
            .imePadding(),
    ) {
        YogheeHeader(
            title = "신규 장소 등록",
            onBack = onBack,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            RegisterSectionTitle(
                title = "수련 위치",
                subTitle = "요기니들이 찾아올 수 있도록 수련 위치를 등록 해주세요.",
                modifier = Modifier.padding(top = 20.dp),
            )
            AddressFieldStatic(label = "국가/지역", value = "대한민국")
            AddressFieldButton(
                label = "광역시/도",
                value = address.depth1,
                onClick = onSearchAddressClick,
            )
            AddressFieldReadOnly(
                label = "시/구",
                value = address.depth2
            )
            AddressFieldReadOnly(
                label = "도로명 주소",
                value = address.roadAddress
            )
            AddressFieldEditable(
                label = "상세 주소",
                value = address.addressDetail,
                onValueChange = { onAddressChange(address.copy(addressDetail = it)) }
            )
            AddressFieldReadOnly(
                label = "우편번호",
                value = address.zonecode
            )
            RegisterSectionTitle(
                title = "수련 장소명",
                subTitle = "요기 지도에 주소록을 저장할 수 있어요! 다음 검색부터 수련 장소명만 입력해보세요.",
                modifier = Modifier.padding(top = 16.dp),
            )
            AddressFieldEditable(
                label = "수련 장소명",
                value = address.name,
                onValueChange = { onAddressChange(address.copy(name = it)) },
            )
            RegisterSectionTitle(
                title = "수련원 상세 위치 설명",
                modifier = Modifier.padding(top = 16.dp),
            )
            HintTextField(
                value = address.description,
                onValueChange = { onAddressChange(address.copy(description = it)) },
                hint1 = "내용",
                hint2 = "",
                maxLength = CONTENT_MAX_LENGTH,
                modifier = Modifier.padding(top = 20.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
        RegisterCenterBottomBar(onRegisterClick = onRegisterClick)
    }
}

@Composable
private fun RegisterCenterBottomBar(
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(SAND_BEIGE)
            .navigationBarsPadding()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 32.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .paint(
                    painter = painterResource(R.drawable.btn_continue_class_register),
                    contentScale = ContentScale.FillBounds,
                )
                .noRippleClickable(onRegisterClick),
            contentAlignment = Alignment.Center,
        ) {
            YogheeText(
                text = "등록",
                color = BLACK,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

data class CenterForm(
    val name: String = "",
    val description: String = "",
    val depth1: String = "",
    val depth2: String = "",
    val depth3: String = "",
    val roadAddress: String = "",
    val jibunAddress: String = "",
    val zonecode: String = "",
    val addressDetail: String = "",
)

private fun CenterForm.applyKakaoResult(result: KakaoAddressResult): CenterForm =
    copy(
        depth1 = result.depth1,
        depth2 = result.depth2,
        depth3 = result.depth3,
        roadAddress = result.roadAddress,
        jibunAddress = result.jibunAddress,
        zonecode = result.zonecode,
    )

@Preview(showBackground = true, showSystemUi = true, name = "RegisterCenterScreen Filled")
@Composable
private fun RegisterCenterScreenFilledPreview() {
    YogheeTheme {
        RegisterCenterContent(
            address = CenterForm(
                name = "힐링 요가 센터",
                description = "도심 속에서 마음과 몸의 힐링을 찾는 요가 센터입니다.",
                depth1 = "서울",
                depth2 = "강남구",
                depth3 = "역삼동",
                roadAddress = "서울 강남구 테헤란로 212",
                zonecode = "06220",
                addressDetail = "멀티캠퍼스 3층",
            ),
            onAddressChange = {},
            onSearchAddressClick = {},
            onRegisterClick = {},
            onBack = {},
        )
    }
}
