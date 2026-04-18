package com.teamyoga.yoghee.core.domain.model

sealed class MainSection {
    data class Banners(val banners: List<MainBanner>) : MainSection()
    data class TodayClasses(val classes: List<TodayClass>) : MainSection()
    data class InterestedCenters(val centers: List<InterestedCenter>) : MainSection()
    data class NewReviews(val reviews: List<NewReview>) : MainSection()
    data class LayoutOrders(val layoutOrders: List<LayoutOrder>) : MainSection()
}
