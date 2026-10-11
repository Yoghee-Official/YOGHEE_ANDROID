package com.teamyoga.yoghee.core.domain.model

/**
 * 클래스 휴일 정책.
 * @param weeklyOffDays 매주 휴무 요일. ISO-8601 기준 (월=1, 화=2, ..., 일=7)
 * @param publicHolidays 휴무일 공휴일 코드 목록.
 */
data class CreateHolidayPolicyParam(
    val weeklyOffDays: List<Int>,
    val publicHolidays: List<String>,
)
