package com.cabify.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun FieldNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = SplashDestination.route
) {

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        splashGraph(navController = navController)
        cartGraph(navController = navController, onBack = {
            navController.popBackStack()
        })
        shoppingGraph(navController = navController, onBack = {
            navController.popBackStack(DashBoardDestination.route, false)
        })
    }
}
