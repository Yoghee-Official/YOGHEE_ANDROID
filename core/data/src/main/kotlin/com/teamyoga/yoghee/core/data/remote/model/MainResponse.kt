//package com.teamyoga.yoghee.core.data.remote.model
//
//import kotlinx.serialization.Serializable
//
//@Serializable
//data class MainResponse(
//    val code: Int,
//    val status: String,
//    val data: MainData
//)
//
//@Serializable
//data class MainData(
//    val todayClass: List<ClassDto> = emptyList(),
//    val imageBanner: List<BannerDto> = emptyList(),
//    val interestedCenter: List<CenterDto> = emptyList(),
//    val newReview: List<ReviewDto> = emptyList(),
//    val layoutOrder: List<LayoutOrderDto> = emptyList()
//)
//
//@Serializable
//data class BannerDto(
//    val classId: String,
//    val className: String,
//    val description: String,
//    val thumbnail: String
//)
//
//@Serializable
//data class CenterDto(
//    val centerId: String,
//    val address: String,
//    val name: String,
//    val thumbnail: String? = null,
//    val favoriteCount: Int,
//    val isFavorite: Boolean
//)
//
//@Serializable
//data class ReviewDto(
//    val reviewId: String,
//    val content: String,
//    val rating: Int,
//    val thumbnail: String? = null
//)
//
//@Serializable
//data class LayoutOrderDto(
//    val order: String,
//    val type: String,
//    val key: String,
//    val text: String? = null
//)
//
//@Serializable
//data class ClassDto(
//    val classId: String,
//    val className: String
//)
