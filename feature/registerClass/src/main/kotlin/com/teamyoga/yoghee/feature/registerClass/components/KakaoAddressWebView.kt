package com.teamyoga.yoghee.feature.registerClass.components

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.json.JSONObject

// Kakao 우편번호 서비스의 oncomplete 콜백에서 사용하는 주요 필드
data class KakaoAddressResult(
    val depth1: String, // 시/도 (예: "서울")
    val depth2: String, // 시/군/구 (예: "강남구")
    val depth3: String, // 법정동/읍/면 (예: "역삼동")
    val roadAddress: String, // 도로명 주소
    val jibunAddress: String, // 지번 주소
    val zonecode: String,
)

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun KakaoAddressWebView(
    onAddressSelected: (KakaoAddressResult) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val currentOnAddressSelected by rememberUpdatedState(onAddressSelected)
    val currentOnClose by rememberUpdatedState(onClose)

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = {
            WebView(context).apply {
                // WebView 기본 배경이 검정색이라 로드 전 잠깐 검게 보이는 것 방지
                setBackgroundColor(Color.WHITE)
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.useWideViewPort = true
                settings.loadWithOverviewMode = true
                webViewClient = WebViewClient()
                addJavascriptInterface(
                    KakaoAddressBridge(
                        onAddressSelected = { currentOnAddressSelected(it) },
                        onClose = { currentOnClose() },
                    ),
                    JS_BRIDGE_NAME,
                )
                val html = context.assets.open(POSTCODE_ASSET_PATH)
                    .bufferedReader().use { it.readText() }
                loadDataWithBaseURL(POSTCODE_BASE_URL, html, "text/html", "UTF-8", null)
            }
        },
    )
}

private const val POSTCODE_ASSET_PATH = "kakao_postcode.html"
private const val POSTCODE_BASE_URL = "https://postcode.local/"
private const val JS_BRIDGE_NAME = "AndroidBridge"

private class KakaoAddressBridge(
    private val onAddressSelected: (KakaoAddressResult) -> Unit,
    private val onClose: () -> Unit,
) {
    // JS 콜백은 WebView 백그라운드 스레드에서 호출되므로 메인 스레드로 포스팅
    private val mainHandler = Handler(Looper.getMainLooper())

    @JavascriptInterface
    fun onAddressSelected(json: String) {
        val result = runCatching {
            val obj = JSONObject(json)
            KakaoAddressResult(
                depth1 = obj.optString("sido"),
                depth2 = obj.optString("sigungu"),
                depth3 = obj.optString("bname"),
                roadAddress = obj.optString("roadAddress"),
                jibunAddress = obj.optString("jibunAddress"),
                zonecode = obj.optString("zonecode"),
            )
        }.getOrNull() ?: return
        mainHandler.post { onAddressSelected(result) }
    }

    @JavascriptInterface
    fun onClose() {
        mainHandler.post { onClose.invoke() }
    }
}
