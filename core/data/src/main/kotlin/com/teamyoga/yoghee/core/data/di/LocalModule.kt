package com.teamyoga.yoghee.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.teamyoga.yoghee.core.data.local.crypto.TokenCipher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Preferences DataStore 인스턴스를 만드는 확장 프로퍼티.
 * 파일 이름("yoghee_auth")은 앱 내부 저장소에 생성될 .preferences_pb 파일명이 된다.
 *
 * preferencesDataStore() delegate 는 프로세스 수준 싱글톤을 보장한다.
 * (같은 파일에 대해 두 번 만들면 런타임 크래시. 그래서 Hilt @Provides가 아니라 top-level로 둔다.)
 */
private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "yoghee_auth"
)

/**
 * 로컬 저장소 관련 의존성 제공 모듈.
 * - DataStore<Preferences>: 토큰을 보관할 비동기 키-값 저장소
 * - TokenCipher: Tink 기반 암호화 wrapper (Android Keystore 초기화 포함)
 */
@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideAuthDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.authDataStore

    @Provides
    @Singleton
    fun provideTokenCipher(
        @ApplicationContext context: Context,
    ): TokenCipher = TokenCipher(context)
}
