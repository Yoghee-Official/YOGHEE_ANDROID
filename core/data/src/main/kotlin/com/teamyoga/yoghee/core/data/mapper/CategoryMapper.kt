package com.teamyoga.yoghee.core.data.mapper

import com.teamyoga.yoghee.core.data.remote.model.CategoryClassDto
import com.teamyoga.yoghee.core.domain.model.CategoryClass

fun CategoryClassDto.toDomain() = CategoryClass(
    classId = classId,
    className = className,
    address = address,
    images = images,
    masterId = masterId,
    masterName = masterName,
    rating = rating,
    review = review,
    price = price,
    favoriteCount = favoriteCount,
    isFavorite = isFavorite,
)
