package com.teamyoga.yoghee.core.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryClassResponse(
    val code: Int,
    val status: String,
    val data: List<CategoryClassDto> = emptyList(),
)

@Serializable
data class CategoryClassDto(
    val classId: String,
    val className: String? = null,
    val address: String? = null,
    val images: List<String> = emptyList(),
    val masterId: String? = null,
    val masterName: String? = null,
    val rating: Double? = null,
    val review: Int? = null,
    val price: Int? = null,
    val favoriteCount: Int? = null,
    val isFavorite: Boolean? = null,
)
