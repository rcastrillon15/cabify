package com.cabify.ui.screem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.cabify.domain.models.ProductModel
import com.cabify.ui.theme.CabifyTheme
import com.cabify.viewmodel.ProductViewModel

@Composable
fun DashBoardScreen(onNavigate: () -> Unit, viewModel: ProductViewModel) {
    val productState by viewModel.productState.collectAsState()

    CabifyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    state = rememberLazyListState()
                ) {
                    itemsIndexed(productState.data) { _, product ->
                        Text(text = product.name)
                    }
                }
            }
        }
    }
}

@Composable
fun ItemCardProduct(movie: ProductModel, onClick: (code: Int) -> Unit){

}