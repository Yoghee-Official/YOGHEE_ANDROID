package com.teamyoga.yoghee.core.data.di

import android.content.Context
import android.content.pm.ApplicationInfo
import com.teamyoga.yoghee.core.data.remote.AuthService
import com.teamyoga.yoghee.core.data.remote.CategoryService
import com.teamyoga.yoghee.core.data.remote.MainService
import com.teamyoga.yoghee.core.data.remote.auth.AuthInterceptor
import com.teamyoga.yoghee.core.data.remote.auth.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

/**
 * AuthService 전용 OkHttp/Retrofit Qualifier
 * (인증 헤더가 필요 없는 경우)
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthHttp

/** 일반 API용 OkHttp/Retrofit Qualifier. AuthInterceptor + TokenAuthenticator가 부착된다. */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AppHttp

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://www.yoghee.xyz/"

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    /**
     * 디버그 빌드에서만 본문 로깅. release에서는 토큰/응답이 logcat에 노출되지 않도록 끔.
     * (BuildConfig.DEBUG는 모듈별 buildConfig 활성화가 필요하므로 ApplicationInfo로 대체)
     */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(
        @ApplicationContext context: Context,
    ): HttpLoggingInterceptor {
        val isDebuggable =
            (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        return HttpLoggingInterceptor().apply {
            level = if (isDebuggable) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    /** AuthService 전용. 인증 헤더/자동 갱신 모두 없음. */
    @Provides
    @Singleton
    @AuthHttp
    fun provideAuthOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    /** 일반 API용. 모든 요청에 Bearer 헤더 자동 부착 + 401 시 자동 refresh. */
    @Provides
    @Singleton
    @AppHttp
    fun provideAppOkHttpClient(
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator,
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        // AuthInterceptor를 먼저 두어 헤더가 부착된 상태로 logging이 출력되도록 한다.
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .authenticator(tokenAuthenticator)
        .build()

    @Provides
    @Singleton
    @AuthHttp
    fun provideAuthRetrofit(
        @AuthHttp okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit = buildRetrofit(okHttpClient, json)

    @Provides
    @Singleton
    @AppHttp
    fun provideAppRetrofit(
        @AppHttp okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit = buildRetrofit(okHttpClient, json)

    @Provides
    @Singleton
    fun provideAuthService(@AuthHttp retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideMainService(@AppHttp retrofit: Retrofit): MainService =
        retrofit.create(MainService::class.java)

    @Provides
    @Singleton
    fun provideCategoryService(@AppHttp retrofit: Retrofit): CategoryService =
        retrofit.create(CategoryService::class.java)

    private fun buildRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
}
