package com.cabify.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.cabify.ui.screen.CartScreen

object CartDestination : NavigationDestination {
    override val route = "cart_route"
    override val destination = "cart_destination"
}

fun NavGraphBuilder.cartGraph(onBack: () -> Unit) {
    composable(route = CartDestination.route) {
        CartScreen(onBack = onBack)
    }
}
