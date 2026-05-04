package com.teamyoga.yoghee.core.data.mapper

import com.teamyoga.yoghee.core.data.remote.model.*
import com.teamyoga.yoghee.core.domain.model.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement

private object MainDataKey {
    const val IMAGE_BANNER = "imageBanner"
    const val INTERESTED_CLASS = "interestedClass"
    const val TODAY_CLASS = "todayClass"
    const val INTERESTED_CENTER = "interestedCenter"

    const val NEW_REVIEW = "newReview"
    const val LAYOUT_ORDER = "layoutOrder"
}

fun MainResponse.toDomain(json: Json): List<MainSection> =
    data.entries.mapNotNull { (key, value) -> json.toSection(key, value) }

private fun Json.toSection(key: String, value: JsonElement): MainSection? = when (key) {
    MainDataKey.IMAGE_BANNER -> MainSection.Banners(decodeList<BannerDto>(value).map { it.toDomain() })
    MainDataKey.INTERESTED_CLASS -> MainSection.InterestedClassList(decodeList<InterestedClassDto>(value).map { it.toDomain() })

    MainDataKey.TODAY_CLASS -> MainSection.TodayClasses(decodeList<ClassDto>(value).map { it.toDomain() })
    MainDataKey.INTERESTED_CENTER -> MainSection.InterestedCenters(decodeList<CenterDto>(value).map { it.toDomain() })
    MainDataKey.NEW_REVIEW -> MainSection.NewReviews(decodeList<ReviewDto>(value).map { it.toDomain() })
    MainDataKey.LAYOUT_ORDER -> MainSection.LayoutOrders(decodeList<LayoutOrderDto>(value).map { it.toDomain() })
    else -> null
}

private inline fun <reified T> Json.decodeList(element: JsonElement): List<T> =
    decodeFromJsonElement(element)

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

private fun LayoutOrderDto.toDomain() = LayoutOrder(
    order = order,
    type = type,
    key = key,
    text = text
)
