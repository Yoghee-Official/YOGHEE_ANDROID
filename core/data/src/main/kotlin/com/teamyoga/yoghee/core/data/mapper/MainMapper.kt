package com.teamyoga.yoghee.core.data.mapper

import com.teamyoga.yoghee.core.data.remote.model.*
import com.teamyoga.yoghee.core.domain.model.*

private object MainDataKey {
    const val IMAGE_BANNER = "imageBanner"
    const val INTERESTED_CLASS = "interestedClass"
    const val TODAY_CLASS = "todayClass"
    const val INTERESTED_CENTER = "interestedCenter"
    const val NEW_REVIEW = "newReview"
}

fun MainResponse.toDomain(): List<MainSection> {
    val sectionsByKey: Map<String, MainSection?> = mapOf(
        MainDataKey.IMAGE_BANNER to data.imageBanner?.let { bannerDtoList ->
            MainSection.Banners(bannerDtoList.map { dto -> dto.toDomain() })
        },
        MainDataKey.INTERESTED_CLASS to data.interestedClass?.let { interestedClassDtoList ->
            val items = interestedClassDtoList.map { dto -> dto.toDomain() }
            if (items.size >= 3) MainSection.InterestedClassList(items) else null
        },
        MainDataKey.TODAY_CLASS to data.todayClass?.let {
            MainSection.TodayClasses(it.map { dto -> dto.toDomain() })
        },
        MainDataKey.INTERESTED_CENTER to data.interestedCenter?.let {
            MainSection.InterestedCenters(it.map { dto -> dto.toDomain() })
        },
        MainDataKey.NEW_REVIEW to data.newReview?.let {
            val items = it.filter { dto -> !dto.thumbnail.isNullOrEmpty() }
                .map { dto -> dto.toDomain() }
            if (items.isNotEmpty()) MainSection.NewReviews(items) else null
        }
    )

    return data.layoutOrder.orEmpty().mapNotNull { sectionsByKey[it.key] }
}

private fun ClassDto.toDomain() = TodayClass(
    classId = classId,
    className = className
)

private fun BannerDto.toDomain() = MainBanner(
    classId = classId,
    className = className,
    description = description,
    thumbnail = thumbnail
)

private fun InterestedClassDto.toDomain() = InterestedClass(
    classId = classId,
    className = className,
    masterId = masterId,
    masterName = masterName,
    review = review,
    price = price,
    rating = rating,
    isFavorite = isFavorite,
    thumbnail = thumbnail
)

private fun CenterDto.toDomain() = InterestedCenter(
    centerId = centerId,
    address = address,
    name = name,
    thumbnail = thumbnail,
    favoriteCount = favoriteCount,
    isFavorite = isFavorite
)

private fun ReviewDto.toDomain() = NewReview(
    reviewId = reviewId,
    userUuid = userUuid,
    thumbnail = thumbnail,
    content = content,
    rating = rating,
    createdAt = createdAt,
    nickname = nickname,
    userLevel = userLevel,
    userProfile = userProfile
)
