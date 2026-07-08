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

    data object OneDayClassRegister : AppRoute("oneDayClassRegister/{typeIndex}") {
        const val ARG_TYPE_INDEX = "typeIndex"

        fun createRoute(typeIndex: Int): String = "oneDayClassRegister/$typeIndex"
    }

    data object Detail : AppRoute("detail/{id}") {
        const val ARG_ID = "id"

        fun createRoute(id: String): String = "detail/$id"
    }
}

