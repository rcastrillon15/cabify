package com.cabify.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.cabify.ui.screem.DashBoardScreen
import com.cabify.ui.screem.SplashScreen

object SplashDestination : NavigationDestination {
    override val route: String = "splash_route"
    override val destination: String = "splash_destination"
}

object DashBoardDestination : NavigationDestination {
    override val route = "dashboard_route"
    override val destination = "dashboard_destination"
}

fun NavGraphBuilder.splashGraph(navController: NavController) {
    composable(route = SplashDestination.route) {
        SplashScreen(navigate = {
            navController.popBackStack()
            navController.navigate(DashBoardDestination.route)
        })
    }

    composable(route = DashBoardDestination.route) {
        DashBoardScreen(
            onNavigate = {
                navController.navigate(CartDestination.route)
            }, viewModel = hiltViewModel()
        )
    }
}
