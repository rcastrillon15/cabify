package com.cabify.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.cabify.ui.screen.ShoppingScreen
import com.cabify.viewmodel.ProductViewModel

object ShoppingDestination : NavigationDestination {
    override val route = "shopping_route"
    override val destination = "shopping_destination"
}

@SuppressLint("UnrememberedGetBackStackEntry")
fun NavGraphBuilder.shoppingGraph(navController: NavController, onBack: () -> Unit) {
    composable(route = ShoppingDestination.route) {

        val viewModel: ProductViewModel =
            hiltViewModel(remember { navController.getBackStackEntry(DashBoardDestination.route) })

        ShoppingScreen(
            onBack = onBack,
            viewModel = viewModel
        )
    }
}
