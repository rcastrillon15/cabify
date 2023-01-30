package com.cabify.ui.screen

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.cabify.viewmodel.ProductViewModel

@Composable
fun ShoppingScreen(
    onBack: () -> Unit,
    viewModel: ProductViewModel
) {
    Text(text = "Shopping")
}
