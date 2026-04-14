package com.teamyoga.yoghee.core.domain.repository

import com.teamyoga.yoghee.core.domain.model.MainHomeData

interface MainRepository {
    suspend fun getMainData(type: String = "R"): MainHomeData
}
