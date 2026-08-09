package com.teamyoga.yoghee.feature.registerClass

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamyoga.yoghee.core.ui.R
import com.teamyoga.yoghee.core.ui.component.YogheeHeader
import com.teamyoga.yoghee.core.ui.component.YogheeText
import com.teamyoga.yoghee.core.ui.theme.BLACK
import com.teamyoga.yoghee.core.ui.theme.LAND_BROWN
import com.teamyoga.yoghee.core.ui.theme.MIND_ORANGE
import com.teamyoga.yoghee.core.ui.theme.SAND_BEIGE
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme
import com.teamyoga.yoghee.core.ui.util.noRippleClickable
import com.teamyoga.yoghee.feature.registerClass.components.AMENITY_FACILITY_OPTIONS
import com.teamyoga.yoghee.feature.registerClass.components.AMENITY_ITEM_OPTIONS
import com.teamyoga.yoghee.feature.registerClass.components.AddressFieldButton
import com.teamyoga.yoghee.feature.registerClass.components.AddressFieldEditable
import com.teamyoga.yoghee.feature.registerClass.components.AddressFieldReadOnly
import com.teamyoga.yoghee.feature.registerClass.components.AddressFieldStatic
import com.teamyoga.yoghee.feature.registerClass.components.CONTENT_MAX_LENGTH
import com.teamyoga.yoghee.feature.registerClass.components.HintTextField
import com.teamyoga.yoghee.feature.registerClass.components.KakaoAddressBottomSheet
import com.teamyoga.yoghee.feature.registerClass.components.KakaoAddressResult
import com.teamyoga.yoghee.feature.registerClass.components.MultiSelectChipsSection
import com.teamyoga.yoghee.feature.registerClass.components.RegisterSectionTitle

@Composable
fun RegisterCenterScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterCenterViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showAddressSheet by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val isLoading = state.submitState is SubmitState.Loading

    LaunchedEffect(state.submitState) {
        when (val submit = state.submitState) {
            SubmitState.Success -> onBack()
            is SubmitState.Error -> {
                snackbarHostState.showSnackbar(submit.message)
                viewModel.onErrorConsumed()
            }
            else -> Unit
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        RegisterCenterContent(
            address = state.form,
            onAddressChange = viewModel::onFormChange,
            onSearchAddressClick = { showAddressSheet = true },
            onRegisterClick = viewModel::submit,
            onBack = onBack,
            isLoading = isLoading,
            showRequiredErrors = state.showRequiredErrors,
        )
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding(),
        )
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .pointerInput(Unit) { detectTapGestures { } },
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = LAND_BROWN)
            }
        }
    }

    if (showAddressSheet) {
        KakaoAddressBottomSheet(
            onDismiss = { showAddressSheet = false },
            onAddressSelected = { result ->
                viewModel.onFormChange(state.form.applyKakaoResult(result))
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
    isLoading: Boolean,
    showRequiredErrors: Boolean,
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
        ) {
            RegisterSectionTitle(
                title = "수련 위치",
                subTitle = "요기니들이 찾아올 수 있도록 수련 위치를 등록 해주세요.",
                modifier = Modifier.padding(top = 20.dp),
            )
            AddressFieldStatic(label = "국가/지역", value = "대한민국", modifier = Modifier.padding(top = 12.dp))
            AddressFieldButton(
                label = "광역시/도",
                value = address.depth1,
                onClick = onSearchAddressClick,
                modifier = Modifier.padding(top = 12.dp),
                required = true,
                isError = showRequiredErrors && address.depth1.isBlank(),
            )
            AddressFieldReadOnly(
                label = "시/구",
                value = address.depth2,
                modifier = Modifier.padding(top = 12.dp),
                required = true,
                isError = showRequiredErrors && address.depth2.isBlank(),
            )
            AddressFieldReadOnly(
                label = "도로명 주소",
                value = address.roadAddress,
                modifier = Modifier.padding(top = 12.dp),
                required = true,
                isError = showRequiredErrors && address.roadAddress.isBlank(),
            )
            AddressFieldEditable(
                label = "상세 주소",
                value = address.addressDetail,
                onValueChange = { onAddressChange(address.copy(addressDetail = it)) },
                modifier = Modifier.padding(top = 12.dp),
                required = false
            )
            AddressFieldReadOnly(
                label = "우편번호",
                value = address.zonecode,
                modifier = Modifier.padding(top = 12.dp),
                required = true,
                isError = showRequiredErrors && address.zonecode.isBlank(),
            )
            RegisterSectionTitle(
                title = "수련 장소명",
                subTitle = "요기 지도에 주소록을 저장할 수 있어요! 다음 검색부터 수련 장소명만 입력해보세요.",
                modifier = Modifier.padding(top = 28.dp),
            )
            AddressFieldEditable(
                label = "수련 장소명",
                value = address.name,
                onValueChange = { onAddressChange(address.copy(name = it))},
                modifier = Modifier.padding(top = 12.dp),
                required = true
            )
            if (showRequiredErrors && address.name.isBlank()) {
                YogheeText(
                    text = "* 필수 입력란을 채워주세요.",
                    color = MIND_ORANGE,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 8.dp, start = 16.dp),
                )
            }
            RegisterSectionTitle(
                title = "수련원 상세 위치 설명",
                modifier = Modifier.padding(top = 28.dp),
            )
            HintTextField(
                value = address.description,
                onValueChange = { onAddressChange(address.copy(description = it)) },
                hint1 = "내용",
                hint2 = "",
                maxLength = CONTENT_MAX_LENGTH,
                modifier = Modifier.padding(top = 8.dp)
            )
            MultiSelectChipsSection(
                title = "수련원에서 제공하는 물품",
                subTitle = "복수 선택 가능",
                options = AMENITY_ITEM_OPTIONS,
                selected = address.amenityCodes,
                onSelectedChange = { onAddressChange(address.copy(amenityCodes = it)) },
                modifier = Modifier.padding(top = 14.dp),
                titleModifier = Modifier.padding(bottom = 12.dp),
            )
            MultiSelectChipsSection(
                title = "수련원 편의시설",
                subTitle = "복수 선택 가능",
                options = AMENITY_FACILITY_OPTIONS,
                selected = address.amenityCodes,
                onSelectedChange = { onAddressChange(address.copy(amenityCodes = it)) },
                modifier = Modifier.padding(top = 14.dp),
                titleModifier = Modifier.padding(bottom = 12.dp),
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
        RegisterCenterBottomBar(
            onRegisterClick = onRegisterClick,
            isLoading = isLoading,
        )
    }
}

@Composable
private fun RegisterCenterBottomBar(
    onRegisterClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(SAND_BEIGE)
            .navigationBarsPadding()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(208.dp)
                .height(48.dp)
                .alpha(if (isLoading) 0.5f else 1f)
                .paint(
                    painter = painterResource(R.drawable.btn_continue_class_register),
                    contentScale = ContentScale.FillBounds,
                )
                .noRippleClickable { if (!isLoading) onRegisterClick() },
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
    val amenityCodes: Set<String> = emptySet(),
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
                amenityCodes = setOf("mat", "wifi", "shower_room"),
            ),
            onAddressChange = {},
            onSearchAddressClick = {},
            onRegisterClick = {},
            onBack = {},
            isLoading = false,
            showRequiredErrors = false,
        )
    }
}
