package com.teamyoga.yoghee.core.domain.repository

import com.teamyoga.yoghee.core.domain.model.MainSection

interface MainRepository {
    suspend fun getMainData(type: String): List<MainSection>
}
