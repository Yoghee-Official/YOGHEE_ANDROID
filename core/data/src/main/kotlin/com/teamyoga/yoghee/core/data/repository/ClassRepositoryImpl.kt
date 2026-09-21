package com.teamyoga.yoghee.core.data.repository

import com.teamyoga.yoghee.core.data.remote.ClassService
import com.teamyoga.yoghee.core.data.remote.model.CenterDetailDto
import com.teamyoga.yoghee.core.data.remote.model.ClassCategoryDto
import com.teamyoga.yoghee.core.data.remote.model.ClassCenterDto
import com.teamyoga.yoghee.core.data.remote.model.ClassDetailDto
import com.teamyoga.yoghee.core.data.remote.model.ClassFeatureDto
import com.teamyoga.yoghee.core.data.remote.model.ClassMasterInfoDto
import com.teamyoga.yoghee.core.data.remote.model.ClassPolicyDto
import com.teamyoga.yoghee.core.data.remote.model.ClassRefundPolicyDto
import com.teamyoga.yoghee.core.data.remote.model.ClassScheduleDto
import com.teamyoga.yoghee.core.data.remote.model.ClassScheduleMonthDto
import com.teamyoga.yoghee.core.data.remote.model.ClassTicketDto
import com.teamyoga.yoghee.core.data.remote.model.CreateCenterRequest
import com.teamyoga.yoghee.core.data.remote.model.CreateClassPolicyDto
import com.teamyoga.yoghee.core.data.remote.model.CreateClassRequest
import com.teamyoga.yoghee.core.data.remote.model.CreateHolidayPolicyDto
import com.teamyoga.yoghee.core.data.remote.model.CreateRefundPolicyDto
import com.teamyoga.yoghee.core.data.remote.model.CreateScheduleDto
import com.teamyoga.yoghee.core.data.remote.model.CreateTicketDto
import com.teamyoga.yoghee.core.data.remote.model.MyCenterDto
import com.teamyoga.yoghee.core.data.remote.model.ReviewDto
import com.teamyoga.yoghee.core.domain.model.Center
import com.teamyoga.yoghee.core.domain.model.CenterDetail
import com.teamyoga.yoghee.core.domain.model.ClassCategory
import com.teamyoga.yoghee.core.domain.model.ClassCenter
import com.teamyoga.yoghee.core.domain.model.ClassDetail
import com.teamyoga.yoghee.core.domain.model.ClassFeature
import com.teamyoga.yoghee.core.domain.model.ClassMasterInfo
import com.teamyoga.yoghee.core.domain.model.ClassPolicy
import com.teamyoga.yoghee.core.domain.model.ClassRefundPolicy
import com.teamyoga.yoghee.core.domain.model.ClassReview
import com.teamyoga.yoghee.core.domain.model.ClassSchedule
import com.teamyoga.yoghee.core.domain.model.ClassScheduleMonth
import com.teamyoga.yoghee.core.domain.model.ClassScheduleParam
import com.teamyoga.yoghee.core.domain.model.ClassTicket
import com.teamyoga.yoghee.core.domain.model.CreateCenterParams
import com.teamyoga.yoghee.core.domain.model.CreateClassPolicyParam
import com.teamyoga.yoghee.core.domain.model.CreateHolidayPolicyParam
import com.teamyoga.yoghee.core.domain.model.CreateOneDayClassParams
import com.teamyoga.yoghee.core.domain.model.CreateRefundPolicyParam
import com.teamyoga.yoghee.core.domain.model.CreateRegularClassParams
import com.teamyoga.yoghee.core.domain.model.RegularClassScheduleParam
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

    override suspend fun createRegularClass(params: CreateRegularClassParams): String {
        val request = CreateClassRequest(
            type = params.type,
            name = params.name,
            description = params.description,
            centerId = params.centerId,
            featureCodes = params.featureCodes,
            categoryCodes = params.categoryCodes,
            schedules = params.schedules.map { it.toDto() },
            images = params.images,
            holidayPolicy = params.holidayPolicy.toDto(),
        )
        return classService.createClass(request).data.orEmpty()
    }

    private fun CreateHolidayPolicyParam.toDto(): CreateHolidayPolicyDto = CreateHolidayPolicyDto(
        weeklyOffDays = weeklyOffDays,
        publicHolidays = publicHolidays,
    )

    private fun RegularClassScheduleParam.toDto(): CreateScheduleDto = CreateScheduleDto(
        dayOfWeek = dayOfWeek,
        startTime = startTime,
        endTime = endTime,
        minCapacity = minCapacity,
        maxCapacity = maxCapacity,
        name = name,
        instructorNote = instructorNote,
    )

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

    override suspend fun getClassDetail(classId: String): ClassDetail {
        val dto = classService.getClassDetail(classId).data
            ?: error("수련 상세 정보를 불러오지 못했습니다.")
        return dto.toDomain()
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

    private fun ClassDetailDto.toDomain(): ClassDetail = ClassDetail(
        classId = classId,
        type = type,
        name = name,
        description = description,
        price = price,
        originalPrice = originalPrice,
        discountRate = discountRate,
        images = images,
        thumbnail = thumbnail,
        categories = categories.map { it.toDomain() },
        features = features.map { it.toDomain() },
        favoriteCount = favoriteCount,
        isFavorite = isFavorite,
        reviewCount = reviewCount,
        rating = rating,
        recentReviews = recentReviews.map { it.toClassReview() },
        policy = policy?.toDomain(),
        schedules = schedules.map { it.toDomain() },
        scheduleYear = scheduleYear,
        scheduleMonth = scheduleMonth,
        scheduleMonths = scheduleMonths.map { it.toDomain() },
        tickets = tickets.map { it.toDomain() },
        center = center?.toDomain(),
        masterInfo = masterInfo?.toDomain(),
    )

    private fun ClassCategoryDto.toDomain(): ClassCategory = ClassCategory(
        code = code,
        name = name,
    )

    private fun ClassFeatureDto.toDomain(): ClassFeature = ClassFeature(
        code = code,
        description = description,
    )

    // MainResponse 의 ReviewDto 를 Class 상세용 ClassReview 로 매핑.
    // 필드 누락 시 안전한 기본값(빈 문자열, 0)을 제공한다.
    private fun ReviewDto.toClassReview(): ClassReview = ClassReview(
        reviewId = reviewId.orEmpty(),
        userUuid = userUuid.orEmpty(),
        nickname = nickname.orEmpty(),
        userLevel = userLevel ?: 0,
        profileUrl = profileUrl.orEmpty(),
        thumbnail = thumbnail.orEmpty(),
        content = content.orEmpty(),
        rating = rating ?: 0.0,
        createdAt = createdAt.orEmpty(),
    )

    private fun ClassPolicyDto.toDomain(): ClassPolicy = ClassPolicy(
        discountRate = discountRate,
        discountStartDate = discountStartDate,
        discountEndDate = discountEndDate,
        reservationNote = reservationNote,
        refundPolicies = refundPolicies.map { it.toDomain() },
    )

    private fun ClassRefundPolicyDto.toDomain(): ClassRefundPolicy = ClassRefundPolicy(
        hoursBeforeClass = hoursBeforeClass,
        refundRate = refundRate,
    )

    private fun ClassScheduleDto.toDomain(): ClassSchedule = ClassSchedule(
        scheduleId = scheduleId,
        sessionId = sessionId,
        sessionStatus = sessionStatus,
        currentCount = currentCount,
        remainingCapacity = remainingCapacity,
        bookable = bookable,
        unavailableReason = unavailableReason,
        alreadyReserved = alreadyReserved,
        dayOfWeek = dayOfWeek,
        specificDate = specificDate,
        startTime = startTime,
        endTime = endTime,
        minCapacity = minCapacity,
        maxCapacity = maxCapacity,
        content = content,
        instructorName = instructorName,
        effectiveFrom = effectiveFrom,
        effectiveTo = effectiveTo,
    )

    private fun ClassScheduleMonthDto.toDomain(): ClassScheduleMonth = ClassScheduleMonth(
        year = year,
        month = month,
    )

    private fun ClassTicketDto.toDomain(): ClassTicket = ClassTicket(
        ticketId = ticketId,
        ticketType = ticketType,
        price = price,
        originalPrice = originalPrice,
        discountRate = discountRate,
        validMonths = validMonths,
        weeklyCount = weeklyCount,
        totalSessions = totalSessions,
    )

    private fun ClassCenterDto.toDomain(): ClassCenter = ClassCenter(
        centerId = centerId,
        name = name,
        description = description,
        thumbnail = thumbnail,
        sido = sido,
        sigungu = sigungu,
        roadAddress = roadAddress,
        jibunAddress = jibunAddress,
        detailAddress = detailAddress,
        latitude = latitude,
        longitude = longitude,
        amenities = amenities,
        facilities = facilities,
    )

    private fun ClassMasterInfoDto.toDomain(): ClassMasterInfo = ClassMasterInfo(
        nickname = nickname,
        profileImage = profileImage,
        introduction = introduction,
        career = career,
        certificate = certificate,
    )
}
