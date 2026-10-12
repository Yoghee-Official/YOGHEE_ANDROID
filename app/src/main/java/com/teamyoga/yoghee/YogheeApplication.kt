package com.teamyoga.yoghee

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class YogheeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Kakao SDK 초기화 (로그인/링크 등 공통)
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        // Kakao Vector Maps v2 초기화 — 상세화면 위치 섹션에서 사용
        KakaoMapSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }
}
