plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.teamyoga.yoghee.core.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:common"))
    implementation(libs.androidx.core.ktx)
    
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    
    // Network
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)

    // Local storage (암호화된 토큰 저장)
    // DataStore: Flow 기반 비동기 Preferences (SharedPreferences의 후속)
    implementation(libs.androidx.datastore.preferences)
    // Tink: Google의 암호화 라이브러리. Android Keystore에 마스터 키를 보관하고
    // 그 키로 토큰을 AES-GCM 암호화/복호화한다.
    implementation(libs.tink.android)
}
