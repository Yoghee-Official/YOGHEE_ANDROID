package com.teamyoga.yoghee.core.domain.model

data class Center(
    val centerId: String,
    val name: String,
    val address: String,
    val createdAt: String,
)

data class CenterDetail(
    val centerId: String,
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
