package com.teamyoga.yoghee.core.domain.model

data class CreateCenterParams(
    val name: String,
    val description: String,
    val depth1: String,
    val depth2: String,
    val depth3: String,
    val roadAddress: String,
    val jibunAddress: String,
    val zonecode: String,
    val addressDetail: String,
    val amenityCodes: List<String>,
    val categoryCodes: List<String>,
)
