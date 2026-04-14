package com.teamyoga.yoghee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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
import com.teamyoga.yoghee.feature.detail.DetailScreen
import com.teamyoga.yoghee.feature.main.MainScreen
import com.teamyoga.yoghee.feature.profile.ProfileScreen
import com.teamyoga.yoghee.feature.search.SearchScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YogheeTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
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
        startDestination = AppRoute.Main.route,
        modifier = modifier,
    ) {
        composable(AppRoute.Main.route) {
            MainScreen(
                onGoSearch = { navController.navigate(AppRoute.Search.route) },
                onGoCategory = { navController.navigate(AppRoute.Category.route) },
                onGoProfile = { navController.navigate(AppRoute.Profile.route) },
                onGoDetail = { navController.navigate(AppRoute.Detail.createRoute(id = "1")) }
            )
        }

        composable(AppRoute.Search.route) {
            SearchScreen(
                onGoMain = { navController.navigate(AppRoute.Main.route) },
            )
        }

        composable(AppRoute.Category.route) {
            CategoryScreen(
                onGoMain = { navController.navigate(AppRoute.Main.route) },
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
