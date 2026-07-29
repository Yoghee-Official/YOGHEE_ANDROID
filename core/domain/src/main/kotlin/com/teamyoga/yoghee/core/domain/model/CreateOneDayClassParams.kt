package com.teamyoga.yoghee.core.domain.model

/**
 * 하루수련 클래스 등록에 필요한 파라미터.
 * 미구현 Step(4~7)의 필드(price, images, tickets 등)와 schedules 세부(시간/요일)는
 * 추후 UI 완성 시 이곳에 추가한다.
 */
data class CreateOneDayClassParams(
    val type: String,
    val name: String,
    val description: String,
    val centerId: String,
    val featureCodes: List<String>,
    val categoryCodes: List<String>,
)
