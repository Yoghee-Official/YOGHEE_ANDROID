package com.teamyoga.yoghee.core.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateClassRequest(
    val type: String,
    val name: String,
    val description: String,
    val centerId: String,
    val featureCodes: List<String> = emptyList(),
    val schedules: List<CreateScheduleDto> = emptyList(),
    val images: List<String> = emptyList(),
    val price: Int = 0,
    val categoryCodes: List<String> = emptyList(),
    val policy: CreateClassPolicyDto? = null,
    val holidayPolicy: CreateHolidayPolicyDto? = null,
    val tickets: List<CreateTicketDto> = emptyList(),
)

@Serializable
data class CreateScheduleDto(
    val scheduleId: String,
    val dates: List<String> = emptyList(),
    val dayOfWeek: Int = 0,
    val startTime: String = "",
    val minCapacity: Int = 0,
    val maxCapacity: Int = 0,
    val endTime: String = "",
    val name: String = "",
    val instructorNote: String = "",
)

@Serializable
data class CreateClassPolicyDto(
    val discountPrice: Int = 0,
    val discountRate: Int = 0,
    val reservationNote: String = "",
    val refundPolicies: List<CreateRefundPolicyDto> = emptyList(),
)

@Serializable
data class CreateRefundPolicyDto(
    val hoursBeforeClass: Int = 0,
    val refundRate: Int = 0,
)

@Serializable
data class CreateHolidayPolicyDto(
    val weeklyOffDays: List<Int> = emptyList(),
    val publicHolidays: List<String> = emptyList(),
)

@Serializable
data class CreateTicketDto(
    val ticketId: String,
    val ticketType: String = "",
    val price: Int = 0,
    val weeklyCount: Int = 0,
    val validMonths: Int = 0,
    val totalSessions: Int = 0,
)

@Serializable
data class CreateClassResponse(
    val code: Int,
    val status: String,
    val data: String? = null,
)
