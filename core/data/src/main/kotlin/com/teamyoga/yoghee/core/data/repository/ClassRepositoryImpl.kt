package com.teamyoga.yoghee.core.data.repository

import com.teamyoga.yoghee.core.data.remote.ClassService
import com.teamyoga.yoghee.core.data.remote.model.CreateClassRequest
import com.teamyoga.yoghee.core.data.remote.model.CreateScheduleDto
import com.teamyoga.yoghee.core.data.remote.model.MyCenterDto
import com.teamyoga.yoghee.core.domain.model.Center
import com.teamyoga.yoghee.core.domain.model.ClassScheduleParam
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
            schedules = params.schedules.map { it.toDto() },
            // TODO: Step 4~7 UI 완성 시 값 채우기
            images = emptyList(),
            price = 0,
            policy = null,
            holidayPolicy = null,
            tickets = emptyList(),
        )
        return classService.createClass(request).data.orEmpty()
    }

    override suspend fun getCenters(): List<Center> =
        classService.getCenters().data.map { it.toDomain() }

    private fun ClassScheduleParam.toDto(): CreateScheduleDto = CreateScheduleDto(
        dates = dates,
        startTime = startTime,
        endTime = endTime,
        minCapacity = minCapacity,
        maxCapacity = maxCapacity,
        name = name,
    )

    private fun MyCenterDto.toDomain(): Center = Center(
        centerId = centerId,
        name = name,
        address = address,
        createdAt = createdAt,
    )
}
