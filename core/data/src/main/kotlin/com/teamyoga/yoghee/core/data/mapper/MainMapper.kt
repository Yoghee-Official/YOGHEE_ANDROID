package com.teamyoga.yoghee.core.data.mapper

import com.teamyoga.yoghee.core.data.remote.model.*
import com.teamyoga.yoghee.core.domain.model.*

private object MainDataKey {
    const val IMAGE_BANNER = "imageBanner"
    const val INTERESTED_CLASS = "interestedClass"
    const val TOP10_CLASS = "top10Class"
    const val TODAY_CLASS = "todayClass"
    const val INTERESTED_CENTER = "interestedCenter"
    const val NEW_REVIEW = "newReview"
}

fun MainResponse.toDomain(): List<MainSection> =
    data.layoutOrder.orEmpty().mapNotNull { entry ->
        when (entry.key) {
            MainDataKey.IMAGE_BANNER -> data.imageBanner?.let { bannerDtoList ->
                MainSection.Banners(
                    banners = bannerDtoList.map { dto -> dto.toDomain() }
                )
            }
            MainDataKey.INTERESTED_CLASS -> data.interestedClass?.let { interestedClassDtoList ->
                val items = interestedClassDtoList.map { dto -> dto.toDomain() }
                if (items.size >= 3) MainSection.InterestedClassList(
                    title = entry.text,
                    interestedClassList = items
                ) else null
            }
            MainDataKey.TOP10_CLASS -> data.top10Class?.let { top10ClassDtoList ->
                val items = top10ClassDtoList.map { dto -> dto.toDomain() }
                if (items.isNotEmpty()) MainSection.Top10Classes(
                    title = entry.text,
                    classes = items
                ) else null
            }
            MainDataKey.TODAY_CLASS -> data.todayClass?.let {
                MainSection.TodayClasses(
                    title = entry.text,
                    classes = it.map { dto -> dto.toDomain() }
                )
            }
            MainDataKey.INTERESTED_CENTER -> data.interestedCenter?.let {
                MainSection.InterestedCenters(
                    title = entry.text,
                    centers = it.map { dto -> dto.toDomain() }
                )
            }
            MainDataKey.NEW_REVIEW -> data.newReview?.let {
                val items = it.filter { dto -> !dto.thumbnail.isNullOrEmpty() }
                    .map { dto -> dto.toDomain() }
                if (items.isNotEmpty()) MainSection.NewReviews(
                    title = entry.text,
                    reviews = items
                ) else null
            }
            else -> null
        }
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
    profileUrl = profileUrl
)
