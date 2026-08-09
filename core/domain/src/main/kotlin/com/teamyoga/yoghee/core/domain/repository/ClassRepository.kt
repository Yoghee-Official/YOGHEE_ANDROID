package com.teamyoga.yoghee.core.domain.repository

import com.teamyoga.yoghee.core.domain.model.Center
import com.teamyoga.yoghee.core.domain.model.CenterDetail
import com.teamyoga.yoghee.core.domain.model.CreateCenterParams
import com.teamyoga.yoghee.core.domain.model.CreateOneDayClassParams

interface ClassRepository {
    suspend fun createOneDayClass(params: CreateOneDayClassParams): String
    suspend fun getCenters(): List<Center>
    suspend fun createCenter(params: CreateCenterParams): String
    suspend fun getCenterDetail(centerId: String): CenterDetail
    suspend fun updateCenter(centerId: String, params: CreateCenterParams): String
}
