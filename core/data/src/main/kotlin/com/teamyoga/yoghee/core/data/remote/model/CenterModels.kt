package com.teamyoga.yoghee.core.data.remote.model

import kotlinx.serialization.Serializable

// 강사 본인이 등록한 요가원. MainResponse.CenterDto(관심 요가원)와 별개.
@Serializable
data class MyCenterDto(
    val centerId: String,
    val name: String,
    val address: String,
    val createdAt: String,
)

@Serializable
data class GetCentersResponse(
    val code: Int,
    val status: String,
    val data: List<MyCenterDto> = emptyList(),
)

// 요가원 등록 요청. centerId / thumbnail / masterId는 서버에서 처리하므로 미포함.
@Serializable
data class CreateCenterRequest(
    val name: String,
    val description: String,
    val depth1: String,
    val depth2: String,
    val depth3: String,
    val roadAddress: String,
    val jibunAddress: String,
    val zonecode: String,
    val addressDetail: String,
    val fullAddress: String,
    val amenityCodes: List<String>,
    val categoryCodes: List<String>,
)

@Serializable
data class CreateCenterResponse(
    val code: Int,
    val status: String,
    val data: CreateCenterData? = null,
)

@Serializable
data class CreateCenterData(
    val centerId: String,
)
