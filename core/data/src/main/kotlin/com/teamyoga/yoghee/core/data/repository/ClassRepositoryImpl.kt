package com.teamyoga.yoghee.core.data.repository

import com.teamyoga.yoghee.core.data.remote.ClassService
import com.teamyoga.yoghee.core.data.remote.model.CreateClassRequest
import com.teamyoga.yoghee.core.domain.model.CreateOneDayClassParams
import com.teamyoga.yoghee.core.domain.repository.ClassRepository
import javax.inject.Inject

class ClassRepositoryImpl @Inject constructor(
    private val classService: ClassService,
) : ClassRepository {

    override suspend fun createOneDayClass(params: CreateOneDayClassParams): String {
        val request = CreateClassRequest(
            type = params.type,
            name = params.name,
            description = params.description,
            centerId = params.centerId,
            featureCodes = params.featureCodes,
            categoryCodes = params.categoryCodes,
            // TODO: Step 3 ScheduleBottomSheet 완성 시 dates/시간/요일 등을 조립
            schedules = emptyList(),
            // TODO: Step 4~7 UI 완성 시 값 채우기
            images = emptyList(),
            price = 0,
            policy = null,
            holidayPolicy = null,
            tickets = emptyList(),
        )
        return classService.createClass(request).data.orEmpty()
    }
}
