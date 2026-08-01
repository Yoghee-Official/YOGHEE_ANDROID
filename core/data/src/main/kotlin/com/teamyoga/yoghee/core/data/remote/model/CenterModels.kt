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
