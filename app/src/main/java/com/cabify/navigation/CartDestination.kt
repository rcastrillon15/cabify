package com.cabify.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.cabify.ui.screen.CartScreen
import com.cabify.viewmodel.ProductViewModel

object CartDestination : NavigationDestination {
    override val route = "cart_route"
    override val destination = "cart_destination"
}

@SuppressLint("UnrememberedGetBackStackEntry")
fun NavGraphBuilder.cartGraph(navController: NavController, onBack: () -> Unit) {
    composable(route = CartDestination.route) {

        val viewModel: ProductViewModel =
            hiltViewModel(remember { navController.getBackStackEntry(DashBoardDestination.route) })

        CartScreen(onBack = onBack, viewModel = viewModel)
    }
}
