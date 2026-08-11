package com.teamyoga.yoghee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.teamyoga.yoghee.core.navigation.AppRoute
import com.teamyoga.yoghee.core.ui.theme.YogheeTheme
import com.teamyoga.yoghee.feature.category.CategoryScreen
import com.teamyoga.yoghee.feature.category.LocationRoute
import com.teamyoga.yoghee.feature.contentFeed.ContentFeedScreen
import com.teamyoga.yoghee.feature.detail.DetailScreen
import com.teamyoga.yoghee.feature.login.LoginRoute
import com.teamyoga.yoghee.feature.main.MainScreen
import com.teamyoga.yoghee.feature.profile.ProfileScreen
import com.teamyoga.yoghee.feature.registerClass.OneDayClassRegisterScreen
import com.teamyoga.yoghee.feature.registerClass.RegisterCenterScreen
import com.teamyoga.yoghee.feature.registerClass.SelectClassTypeScreen
import com.teamyoga.yoghee.feature.search.SearchScreen
import com.teamyoga.yoghee.splash.SplashScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YogheeTheme {
                val navController = rememberNavController()
                AppNavGraph(
                    navController = navController,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.Splash.route,
        modifier = modifier,
    ) {
        composable(AppRoute.Splash.route) {
            // 토큰 상태 확정 후 Main으로 이동, Splash는 back stack에서 제거
            SplashScreen(
                onReady = {
                    navController.navigate(AppRoute.Main.route) {
                        popUpTo(AppRoute.Splash.route) { inclusive = true }
                    }
                },
            )
        }

        composable(AppRoute.Main.route) {
            MainScreen(
                onGoSearch = { navController.navigate(AppRoute.Search.route) },
                onGoCategory = { navController.navigate(AppRoute.Category.route) },
                onGoLocation = { navController.navigate(AppRoute.Location.route) },
                onGoProfile = { navController.navigate(AppRoute.Profile.route) },
                onGoDetail = { navController.navigate(AppRoute.Detail.createRoute(id = "1")) },
                onGoLogin = { navController.navigate(AppRoute.Login.route) },
                onGoContentFeed = { navController.navigate(AppRoute.ContentFeed.route) },
                onGoRegisterClass = { navController.navigate(AppRoute.RegisterClass.route) },
            )
        }

        composable(AppRoute.ContentFeed.route) {
            ContentFeedScreen(
                onBack = { navController.popBackStack() },
            )
        }

        composable(AppRoute.RegisterClass.route) {
            SelectClassTypeScreen(
                onBack = { navController.popBackStack() },
                onGoOneDayClassRegister = { typeIndex ->
                    navController.navigate(AppRoute.OneDayClassRegister.createRoute(typeIndex = typeIndex))
                },
            )
        }

        composable(
            route = AppRoute.OneDayClassRegister.route,
            arguments = listOf(
                navArgument(AppRoute.OneDayClassRegister.ARG_TYPE_INDEX) { type = NavType.IntType },
            ),
        ) { backStackEntry ->
            val typeIndex = backStackEntry.arguments?.getInt(AppRoute.OneDayClassRegister.ARG_TYPE_INDEX) ?: 0
            OneDayClassRegisterScreen(
                typeIndex = typeIndex,
                onBack = { navController.popBackStack() },
                onGoRegisterCenter = { navController.navigate(AppRoute.RegisterCenter.createRoute()) },
                onGoEditCenter = { centerId ->
                    navController.navigate(AppRoute.RegisterCenter.createRoute(centerId = centerId))
                },
            )
        }

        composable(
            route = AppRoute.RegisterCenter.route,
            arguments = listOf(
                navArgument(AppRoute.RegisterCenter.ARG_CENTER_ID) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
            ),
        ) {
            RegisterCenterScreen(
                onBack = { navController.popBackStack() },
            )
        }

        composable(AppRoute.Login.route) {
            LoginRoute(
                onLoginSuccess = { // 로그인 성공 시 이전 화면으로 복귀
                    navController.popBackStack(AppRoute.Login.route, inclusive = true)
                },
                onNaverClick = { navController.popBackStack() },
                onGoogleClick = { navController.popBackStack() },
                onAppleClick = { navController.popBackStack() },
            )
        }

        composable(AppRoute.Search.route) {
            SearchScreen(
                onGoMain = { navController.navigate(AppRoute.Main.route) },
            )
        }

        composable(AppRoute.Category.route) {
            CategoryScreen(
                onBack = { navController.popBackStack() },
            )
        }

        composable(AppRoute.Location.route) {
            LocationRoute(
                onBack = { navController.popBackStack() },
            )
        }

        composable(AppRoute.Profile.route) {
            ProfileScreen(
                onGoMain = { navController.navigate(AppRoute.Main.route) },
            )
        }

        composable(
            route = AppRoute.Detail.route,
            arguments = listOf(navArgument(AppRoute.Detail.ARG_ID) { type = NavType.StringType }),
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString(AppRoute.Detail.ARG_ID).orEmpty()
            DetailScreen(
                id = id,
                onGoMain = { navController.navigate(AppRoute.Main.route) },
            )
        }
    }
}
