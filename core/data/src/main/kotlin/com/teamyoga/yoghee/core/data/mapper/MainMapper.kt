package com.teamyoga.yoghee.core.data.mapper

import com.teamyoga.yoghee.core.data.remote.model.*
import com.teamyoga.yoghee.core.domain.model.*

fun MainResponse.toDomain(): MainHomeData {
    return MainHomeData(
        todayClasses = data.todayClass.map { it.toDomain() },
        banners = data.imageBanner.map { it.toDomain() },
        centers = data.interestedCenter.map { it.toDomain() },
        reviews = data.newReview.map { it.toDomain() },
        layoutOrder = data.layoutOrder.map { it.toDomain() }
    )
}

fun ClassDto.toDomain() = TodayClass(
    classId = classId,
    className = className
)

fun BannerDto.toDomain() = MainBanner(
    classId = classId,
    className = className,
    description = description,
    thumbnail = thumbnail
)

fun CenterDto.toDomain() = InterestedCenter(
    centerId = centerId,
    address = address,
    name = name,
    thumbnail = thumbnail,
    favoriteCount = favoriteCount,
    isFavorite = isFavorite
)

fun ReviewDto.toDomain() = NewReview(
    reviewId = reviewId,
    content = content,
    rating = rating,
    thumbnail = thumbnail
)

fun LayoutOrderDto.toDomain() = LayoutOrder(
    order = order,
    type = type,
    key = key,
    text = text
)
