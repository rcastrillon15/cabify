package com.cabify.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.cabify.R
import com.cabify.common.toEuro
import com.cabify.domain.models.ProductModel
import com.cabify.ui.theme.large
import com.cabify.ui.theme.normal
import com.cabify.ui.theme.small
import com.cabify.viewmodel.ProductViewModel

@Composable
fun DashBoardScreen(onNavigate: () -> Unit, viewModel: ProductViewModel) {
    val productState by viewModel.productState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    Button(
                        onClick = onNavigate,
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ShoppingCart,
                            contentDescription = "Shopping cart"
                        )
                        Text(
                            text = "99+",
                            style = MaterialTheme.typography.caption.copy(
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                state = rememberLazyListState()
            ) {
                itemsIndexed(productState.data) { _, product ->
                    ItemCardProduct(product = product, onClick = {
                        println("Selected id: $it")
                        onNavigate()
                    })
                }
            }
        }
    }
}

@Composable
fun ItemCardProduct(product: ProductModel, onClick: (code: String) -> Unit) {
    Card(
        shape = RoundedCornerShape(normal),
        modifier = Modifier
            .fillMaxWidth()
            .padding(normal)
            .padding(horizontal = large, vertical = small)
            .clickable { onClick(product.code) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)

        ) {

            Image(
                painter = rememberAsyncImagePainter(product.imageUrl),
                contentDescription = "Product Image",
                modifier = Modifier
                    .height(200.dp)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Crop
            )

            Text(
                text = product.name,
                modifier = Modifier.padding(horizontal = normal),
                style = MaterialTheme.typography.h6.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = stringResource(R.string.price, product.price.toEuro()),
                modifier = Modifier.padding(horizontal = normal),
                style = MaterialTheme.typography.subtitle2.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}
