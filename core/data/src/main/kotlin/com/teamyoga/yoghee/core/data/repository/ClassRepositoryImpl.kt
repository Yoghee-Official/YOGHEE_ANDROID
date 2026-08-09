package com.teamyoga.yoghee.core.data.repository

import com.teamyoga.yoghee.core.data.remote.ClassService
import com.teamyoga.yoghee.core.data.remote.model.CenterDetailDto
import com.teamyoga.yoghee.core.data.remote.model.CreateCenterRequest
import com.teamyoga.yoghee.core.data.remote.model.CreateClassRequest
import com.teamyoga.yoghee.core.data.remote.model.CreateScheduleDto
import com.teamyoga.yoghee.core.data.remote.model.MyCenterDto
import com.teamyoga.yoghee.core.domain.model.Center
import com.teamyoga.yoghee.core.domain.model.CenterDetail
import com.teamyoga.yoghee.core.domain.model.ClassScheduleParam
import com.teamyoga.yoghee.core.domain.model.CreateCenterParams
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

    override suspend fun createCenter(params: CreateCenterParams): String {
        val request = params.toRequest()
        return classService.createCenter(request).data?.centerId.orEmpty()
    }

    override suspend fun getCenterDetail(centerId: String): CenterDetail {
        val dto = classService.getCenterDetail(centerId).data
            ?: error("요가원 상세 정보를 불러오지 못했습니다.")
        return dto.toDomain()
    }

    override suspend fun updateCenter(centerId: String, params: CreateCenterParams): String {
        val request = params.toRequest()
        classService.updateCenter(centerId, request)
        return centerId
    }

    private fun CreateCenterParams.toRequest(): CreateCenterRequest {
        val fullAddress = buildString {
            append(roadAddress)
            if (addressDetail.isNotBlank()) {
                if (isNotEmpty()) append(' ')
                append(addressDetail)
            }
        }
        return CreateCenterRequest(
            name = name,
            description = description,
            depth1 = depth1,
            depth2 = depth2,
            depth3 = depth3,
            roadAddress = roadAddress,
            jibunAddress = jibunAddress,
            zonecode = zonecode,
            addressDetail = addressDetail,
            fullAddress = fullAddress,
            amenityCodes = amenityCodes,
            categoryCodes = categoryCodes,
        )
    }

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

    private fun CenterDetailDto.toDomain(): CenterDetail = CenterDetail(
        centerId = centerId,
        name = name,
        description = description,
        depth1 = depth1,
        depth2 = depth2,
        depth3 = depth3,
        roadAddress = roadAddress,
        jibunAddress = jibunAddress,
        zonecode = zonecode,
        addressDetail = addressDetail,
        amenityCodes = amenityCodes,
        categoryCodes = categoryCodes,
    )
}
