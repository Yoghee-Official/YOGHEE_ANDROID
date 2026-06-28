package com.teamyoga.yoghee.core.data.di

import com.teamyoga.yoghee.core.data.repository.AuthRepositoryImpl
import com.teamyoga.yoghee.core.data.repository.CategoryRepositoryImpl
import com.teamyoga.yoghee.core.data.repository.FeedRepositoryImpl
import com.teamyoga.yoghee.core.data.repository.MainRepositoryImpl
import com.teamyoga.yoghee.core.domain.repository.AuthRepository
import com.teamyoga.yoghee.core.domain.repository.CategoryRepository
import com.teamyoga.yoghee.core.domain.repository.FeedRepository
import com.teamyoga.yoghee.core.domain.repository.MainRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindMainRepository(
        mainRepositoryImpl: MainRepositoryImpl
    ): MainRepository

    @Binds
    @Singleton
    fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    @Singleton
    fun bindFeedRepository(
        feedRepositoryImpl: FeedRepositoryImpl
    ): FeedRepository
}
