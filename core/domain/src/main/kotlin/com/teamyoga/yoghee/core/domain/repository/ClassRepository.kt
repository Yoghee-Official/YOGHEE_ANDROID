package com.teamyoga.yoghee.core.domain.repository

import com.teamyoga.yoghee.core.domain.model.CreateOneDayClassParams

interface ClassRepository {
    suspend fun createOneDayClass(params: CreateOneDayClassParams): String
}
