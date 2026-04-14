package com.teamyoga.yoghee.core.domain.model

data class MainHomeData(
    val todayClasses: List<TodayClass>,
    val banners: List<MainBanner>,
    val centers: List<InterestedCenter>,
    val reviews: List<NewReview>,
    val layoutOrder: List<LayoutOrder>
)

data class TodayClass(
    val classId: String,
    val className: String
)

data class MainBanner(
    val classId: String,
    val className: String,
    val description: String,
    val thumbnail: String
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
    val reviewId: String,
    val content: String,
    val rating: Int,
    val thumbnail: String?
)

data class LayoutOrder(
    val order: String,
    val type: String,
    val key: String,
    val text: String?
)
