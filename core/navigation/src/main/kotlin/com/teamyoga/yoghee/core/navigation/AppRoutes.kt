package com.teamyoga.yoghee.core.navigation

sealed class AppRoute(val route: String) {
    data object Splash : AppRoute("splash")
    data object Login : AppRoute("login")
    data object Main : AppRoute("main")
    data object Search : AppRoute("search")
    data object Category : AppRoute("category")
    data object Location : AppRoute("location")
    data object Profile : AppRoute("profile")
    data object ContentFeed : AppRoute("contentFeed")
    data object RegisterClass : AppRoute("registerClass")

    // centerId가 있으면 편집 모드, 없으면 신규 등록.
    data object RegisterCenter : AppRoute("registerCenter?centerId={centerId}") {
        const val ARG_CENTER_ID = "centerId"

        fun createRoute(centerId: String? = null): String =
            if (centerId.isNullOrBlank()) "registerCenter" else "registerCenter?centerId=$centerId"
    }

    data object OneDayClassRegister : AppRoute("oneDayClassRegister/{typeIndex}") {
        const val ARG_TYPE_INDEX = "typeIndex"

        fun createRoute(typeIndex: Int): String = "oneDayClassRegister/$typeIndex"
    }

    data object Detail : AppRoute("detail/{id}") {
        const val ARG_ID = "id"

        fun createRoute(id: String): String = "detail/$id"
    }
}

