package com.teamyoga.yoghee.core.domain.model

data class CategoryClass(
    val classId: String,
    val className: String?,
    val address: String?,
    val images: List<String>,
    val masterId: String?,
    val masterName: String?,
    val rating: Double?,
    val review: Int?,
    val price: Int?,
    val favoriteCount: Int?,
    val isFavorite: Boolean?,
)
