package com.teamyoga.yoghee.core.data.repository

import com.teamyoga.yoghee.core.data.remote.ClassService
import com.teamyoga.yoghee.core.data.remote.model.CenterDetailDto
import com.teamyoga.yoghee.core.data.remote.model.CreateCenterRequest
import com.teamyoga.yoghee.core.data.remote.model.CreateClassPolicyDto
import com.teamyoga.yoghee.core.data.remote.model.CreateClassRequest
import com.teamyoga.yoghee.core.data.remote.model.CreateRefundPolicyDto
import com.teamyoga.yoghee.core.data.remote.model.CreateScheduleDto
import com.teamyoga.yoghee.core.data.remote.model.CreateTicketDto
import com.teamyoga.yoghee.core.data.remote.model.MyCenterDto
import com.teamyoga.yoghee.core.domain.model.Center
import com.teamyoga.yoghee.core.domain.model.CenterDetail
import com.teamyoga.yoghee.core.domain.model.ClassScheduleParam
import com.teamyoga.yoghee.core.domain.model.CreateCenterParams
import com.teamyoga.yoghee.core.domain.model.CreateClassPolicyParam
import com.teamyoga.yoghee.core.domain.model.CreateOneDayClassParams
import com.teamyoga.yoghee.core.domain.model.CreateRefundPolicyParam
import com.teamyoga.yoghee.core.domain.repository.ClassRepository
import javax.inject.Inject

private const val ONE_DAY_TICKET_TYPE = "ONE_DAY"

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
            images = params.images,
            price = params.price,
            policy = params.policy?.toDto(),
            // 하루수련은 항상 ONE_DAY 티켓 1장이며 가격은 class price와 동일.
            tickets = listOf(
                CreateTicketDto(
                    ticketType = ONE_DAY_TICKET_TYPE,
                    price = params.price,
                )
            ),
            // TODO: 휴일 UI 미구현
            holidayPolicy = null,
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

    private fun CreateClassPolicyParam.toDto(): CreateClassPolicyDto = CreateClassPolicyDto(
        discountPrice = discountPrice,
        discountRate = discountRate,
        reservationNote = reservationNote,
        refundPolicies = refundPolicies.map { it.toDto() },
    )

    private fun CreateRefundPolicyParam.toDto(): CreateRefundPolicyDto = CreateRefundPolicyDto(
        hoursBeforeClass = hoursBeforeClass,
        refundRate = refundRate,
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
