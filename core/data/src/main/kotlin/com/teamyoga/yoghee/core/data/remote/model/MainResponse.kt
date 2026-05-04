package com.teamyoga.yoghee.core.data.remote.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class MainResponse(
    val code: Int,
    val status: String,
    val data: JsonObject
)

@Serializable
data class BannerDto(
    val classId: String? = null,
    val className: String? = null,
    val description: String? = null,
    val thumbnail: String? = null
)

@Serializable
data class InterestedClassDto(
    val classId: String? = null,
    val className: String? = null,
    val masterId: String? = null,
    val masterName: String? = null,
    val review: Int? = null,
    val price: Int? = null,
    val rating: Double? = null,
    val isFavorite: Boolean? = null,
    val thumbnail: String? = null
)
@Serializable
data class CenterDto(
    val centerId: String,
    val address: String,
    val name: String,
    val thumbnail: String? = null,
    val favoriteCount: Int,
    val isFavorite: Boolean
)


@Serializable
data class ReviewDto(
    val reviewId: String? = null,
    val userUuid: String? = null,
    val thumbnail: String? = null,
    val content: String? = null,
    val rating: Double? = null,
    val createdAt: String? = null,
    val nickname: String? = null,
    val userLevel: Int? = null,
    val userProfile: String? = null
)

@Serializable
data class LayoutOrderDto(
    val order: String,
    val type: String,
    val key: String,
    val text: String? = null
)

@Serializable
data class ClassDto(
    val classId: String,
    val className: String
)
