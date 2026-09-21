package com.teamyoga.yoghee.core.domain.model

/**
 * 정규수련 클래스 등록에 필요한 파라미터.
 * 미구현 Step(2~6)의 필드는 추후 UI 완성 시 이곳에 추가한다.
 */
data class CreateRegularClassParams(
    val type: String,
    val name: String,
    val description: String,
    val centerId: String,
    val featureCodes: List<String>,
    val categoryCodes: List<String>,
    val schedules: List<RegularClassScheduleParam> = emptyList(),
    // 업로드가 끝난 imageUrl 목록. 첫 번째 항목이 썸네일.
    val images: List<String> = emptyList(),
    val holidayPolicy: CreateHolidayPolicyParam = CreateHolidayPolicyParam(
        weeklyOffDays = emptyList(),
        publicHolidays = emptyList(),
    ),
)
