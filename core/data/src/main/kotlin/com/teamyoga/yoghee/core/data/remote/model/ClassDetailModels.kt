package com.teamyoga.yoghee.core.data.remote.model

import kotlinx.serialization.Serializable

// GET /api/class/{classId} 응답. 모든 필드에 기본값을 부여해서 서버 필드 누락 시에도 안전하게 파싱한다.
@Serializable
data class ClassDetailResponse(
    val code: Int,
    val status: String,
    val data: ClassDetailDto? = null,
)

@Serializable
data class ClassDetailDto(
    val classId: String = "",
    val type: String = "",
    val name: String = "",
    val description: String = "",
    val price: Int = 0,
    val originalPrice: Int = 0,
    val discountRate: Int = 0,
    val images: List<String> = emptyList(),
    val thumbnail: String = "",
    val categories: List<ClassCategoryDto> = emptyList(),
    val features: List<ClassFeatureDto> = emptyList(),
    val favoriteCount: Int = 0,
    val isFavorite: Boolean = false,
    val reviewCount: Int = 0,
    val rating: Double = 0.0,
    val recentReviews: List<ReviewDto> = emptyList(),
    val policy: ClassPolicyDto? = null,
    val schedules: List<ClassScheduleDto> = emptyList(),
    val scheduleYear: Int = 0,
    val scheduleMonth: Int = 0,
    val scheduleMonths: List<ClassScheduleMonthDto> = emptyList(),
    val tickets: List<ClassTicketDto> = emptyList(),
    val center: ClassCenterDto? = null,
    val masterInfo: ClassMasterInfoDto? = null,
)

@Serializable
data class ClassCategoryDto(
    val code: String = "",
    val name: String = "",
)

@Serializable
data class ClassFeatureDto(
    val code: String = "",
    val description: String = "",
)

@Serializable
data class ClassPolicyDto(
    val discountRate: Int = 0,
    val discountStartDate: String = "",
    val discountEndDate: String = "",
    val reservationNote: String = "",
    val refundPolicies: List<ClassRefundPolicyDto> = emptyList(),
)

@Serializable
data class ClassRefundPolicyDto(
    val hoursBeforeClass: Int = 0,
    val refundRate: Int = 0,
)

@Serializable
data class ClassScheduleDto(
    val scheduleId: String = "",
    val sessionId: String = "",
    val sessionStatus: String = "",
    val currentCount: Int = 0,
    val remainingCapacity: Int = 0,
    val bookable: Boolean = false,
    val unavailableReason: String = "",
    val alreadyReserved: Boolean = false,
    val dayOfWeek: Int = 0,
    val specificDate: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val minCapacity: Int = 0,
    val maxCapacity: Int = 0,
    val content: String = "",
    val instructorName: String = "",
    val effectiveFrom: String = "",
    val effectiveTo: String = "",
)

@Serializable
data class ClassScheduleMonthDto(
    val year: Int = 0,
    val month: Int = 0,
)

@Serializable
data class ClassTicketDto(
    val ticketId: String = "",
    val ticketType: String = "",
    val price: Int = 0,
    val originalPrice: Int = 0,
    val discountRate: Int = 0,
    val validMonths: Int = 0,
    val weeklyCount: Int = 0,
    val totalSessions: Int = 0,
)

@Serializable
data class ClassCenterDto(
    val centerId: String = "",
    val name: String = "",
    val description: String = "",
    val thumbnail: String = "",
    val sido: String = "",
    val sigungu: String = "",
    val roadAddress: String = "",
    val jibunAddress: String = "",
    val detailAddress: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val amenities: List<String> = emptyList(),
    val facilities: List<String> = emptyList(),
)

@Serializable
data class ClassMasterInfoDto(
    val nickname: String = "",
    val profileImage: String = "",
    val introduction: String = "",
    val career: Int = 0,
    val certificate: String = "",
)
