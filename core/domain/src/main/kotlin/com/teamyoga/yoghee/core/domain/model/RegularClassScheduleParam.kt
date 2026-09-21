package com.teamyoga.yoghee.core.domain.model

/**
 * 정규수련 스케줄 파라미터.
 * @param dayOfWeek ISO-8601 기준 요일 (월=1, 화=2, ..., 일=7)
 */
data class RegularClassScheduleParam(
    val name: String,
    val dayOfWeek: Int,
    val startTime: String,
    val endTime: String,
    val minCapacity: Int,
    val maxCapacity: Int,
    val instructorNote: String,
)
