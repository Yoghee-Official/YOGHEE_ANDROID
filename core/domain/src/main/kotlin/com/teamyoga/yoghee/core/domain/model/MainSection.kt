package com.teamyoga.yoghee.core.domain.model

sealed class MainSection {
    data class Banners(val banners: List<MainBanner>) : MainSection()

    data class InterestedClassList(
        val title: String?,
        val interestedClassList: List<InterestedClass>
    ) : MainSection()

    data class Top10Classes(
        val title: String?,
        val classes: List<InterestedClass>
    ) : MainSection()

    data class YogaCategory(val title: String?) : MainSection()

    data class TodayClasses(
        val title: String?,
        val classes: List<TodayClass>
    ) : MainSection()

    data class InterestedCenters(
        val title: String?,
        val centers: List<InterestedCenter>
    ) : MainSection()

    data class NewReviews(
        val title: String?,
        val reviews: List<NewReview>
    ) : MainSection()
}
