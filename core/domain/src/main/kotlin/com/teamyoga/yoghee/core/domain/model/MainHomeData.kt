package com.teamyoga.yoghee.core.domain.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.String

data class TodayClass(
    val classId: String,
    val className: String
)

data class MainBanner(
    val classId: String?,
    val className: String?,
    val description: String?,
    val thumbnail: String?
)

data class InterestedClass(
    val classId: String?,
    val className: String?,
    val masterId: String?,
    val masterName: String?,
    val review: Int?,
    val price: Int?,
    val rating: Double?,
    val isFavorite: Boolean?,
    val thumbnail: String?
)

data class InterestedCenter(
    val centerId: String,
    val address: String,
    val name: String,
    val thumbnail: String?,
    val favoriteCount: Int,
    val isFavorite: Boolean
)

data class NewReview(
    val reviewId: String?,
    val userUuid: String?,
    val thumbnail: String?,
    val content: String?,
    val rating: Double?,
    val createdAt: String?,
    val nickname: String?,
    val userLevel: Int?,
    val userProfile: String?
)

