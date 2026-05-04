package com.teamyoga.yoghee.core.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class MainResponse(
    val code: Int,
    val status: String,
    val data: MainData
)

@Serializable
data class MainData(
    val imageBanner: List<BannerDto>? = null,
    val interestedClass: List<InterestedClassDto>? = null,
    val todayClass: List<ClassDto>? = null,
    val interestedCenter: List<CenterDto>? = null,
    val newReview: List<ReviewDto>? = null,
    val layoutOrder: List<LayoutOrderDto>? = null
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
    val centerId: String? = null,
    val address: String? = null,
    val name: String? = null,
    val thumbnail: String? = null,
    val favoriteCount: Int? = null,
    val isFavorite: Boolean? = null
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
    val profileUrl: String? = null
)

@Serializable
data class LayoutOrderDto(
    val order: String? = null,
    val type: String? = null,
    val key: String? = null,
    val text: String? = null
)

@Serializable
data class ClassDto(
    val classId: String? = null,
    val className: String? = null
)
