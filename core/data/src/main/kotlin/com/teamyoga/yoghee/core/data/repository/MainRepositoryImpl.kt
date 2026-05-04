package com.teamyoga.yoghee.core.data.repository

import com.teamyoga.yoghee.core.data.mapper.toDomain
import com.teamyoga.yoghee.core.data.remote.MainService
import com.teamyoga.yoghee.core.domain.model.MainSection
import com.teamyoga.yoghee.core.domain.repository.MainRepository
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val mainService: MainService
) : MainRepository {
    override suspend fun getMainData(type: String): List<MainSection> {
        return mainService.getMainData(type).toDomain()
    }
}
