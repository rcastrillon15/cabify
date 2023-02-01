package com.cabify.ui.screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.cabify.R
import com.cabify.common.toEuro
import com.cabify.components.LinearProgressBarCustom
import com.cabify.components.LoadErrorScreen
import com.cabify.components.RatingBar
import com.cabify.domain.models.ProductModel
import com.cabify.ui.theme.Gray_FFF8F8F8
import com.cabify.ui.theme.Purple700
import com.cabify.ui.theme.medium
import com.cabify.ui.theme.normal
import com.cabify.ui.theme.regular
import com.cabify.ui.theme.small
import com.cabify.ui.theme.xsmall
import com.cabify.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun DashBoardScreen(onNavigate: () -> Unit, viewModel: ProductViewModel) {

    val context = LocalContext.current
    val productState by viewModel.productState.collectAsState()
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    var productSelected by remember { mutableStateOf(ProductModel()) }

    BackHandler(bottomSheetState.isVisible) {
        scope.launch {
            bottomSheetState.hide()
        }
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            DetailProduct(product = productSelected, onClick = { product ->
                viewModel.validateStock(product = product, inStock = {
                    if (it) viewModel.updateProduct(product.apply { itemAdded.value += 1 })
                })
            },
                onClose = {
                    scope.launch { bottomSheetState.hide() }
                })
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    backgroundColor = Color.White,
                    contentColor = Purple700,
                    title = { Text(text = stringResource(R.string.app_name)) },
                    actions = {
                        Button(
                            enabled = viewModel.countAddedProducts.value != 0,
                            onClick = {
                                onNavigate()
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                            shape = RoundedCornerShape(28.dp),
                            modifier = Modifier.padding(horizontal = regular)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ShoppingCart,
                                tint = Purple700,
                                contentDescription = "Shopping cart"
                            )
                            Text(
                                text = viewModel.countAddedProducts.value.toString(),
                                style = MaterialTheme.typography.caption.copy(
                                    textAlign = TextAlign.Center,
                                    color = Purple700,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            when {
                productState.isLoading -> {
                    LinearProgressBarCustom()
                }
                productState.failed -> {
                    LoadErrorScreen(viewModel.stateErrorMessage)
                }
                else -> {
                    Column(Modifier.padding(paddingValues)) {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            state = rememberLazyListState()
                        ) {
                            itemsIndexed(productState.data) { _, product ->
                                ItemCardProduct(product = product, onClick = {
                                    productSelected = it
                                    scope.launch {
                                        bottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
                                    }
                                })
                            }
                        }
                    }

                    if (viewModel.showMessage.value) {
                        Toast.makeText(
                            context,
                            stringResource(id = R.string.no_stock),
                            Toast.LENGTH_LONG
                        ).show()
                        viewModel.showMessage.value = false
                    }
                }
            }
        }
    }
}

@Composable
fun ItemCardProduct(product: ProductModel, onClick: (selectedProduct: ProductModel) -> Unit) {
    var isFavorite by remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(normal),
        modifier = Modifier
            .fillMaxWidth()
            .padding(normal)
            .padding(horizontal = regular, vertical = small)
            .clickable { onClick(product) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Gray_FFF8F8F8)
                .height(200.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = product.name,
                    modifier = Modifier.padding(horizontal = normal),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.h6.copy(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                )

                IconToggleButton(
                    checked = isFavorite,
                    onCheckedChange = {
                        isFavorite = !isFavorite
                    }
                ) {
                    Icon(
                        tint = Purple700,
                        modifier = Modifier.graphicsLayer {
                            scaleX = 1.3f
                            scaleY = 1.3f
                        },
                        imageVector = if (isFavorite) {
                            Icons.Filled.Favorite
                        } else {
                            Icons.Default.FavoriteBorder
                        },
                        contentDescription = null
                    )
                }
            }

            Row {
                Image(
                    painter = rememberAsyncImagePainter(product.imageUrl),
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .height(140.dp)
                        .width(120.dp)
                        .padding(start = normal),
                    contentScale = ContentScale.Inside
                )

                Column {

                    Text(
                        text = stringResource(R.string.price, product.price.toEuro()),
                        modifier = Modifier.padding(horizontal = normal, vertical = regular),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.subtitle1.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    Text(
                        text = product.description,
                        modifier = Modifier.padding(horizontal = normal),
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.caption.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Light
                        )
                    )

                    Text(
                        text = stringResource(
                            id = R.string.stock,
                            product.stock - product.itemAdded.value
                        ),
                        modifier = Modifier
                            .padding(horizontal = normal, vertical = medium)
                            .align(Alignment.End),
                        style = MaterialTheme.typography.subtitle2.copy(
                            color = if ((product.stock - product.itemAdded.value) != 0) Color.Black else Color.Red,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun DetailProduct(
    product: ProductModel,
    onClick: (selectedProduct: ProductModel) -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(medium)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        IconButton(
            onClick = { onClose() },
            modifier = Modifier.padding(vertical = xsmall, horizontal = small)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Close",
                tint = Purple700
            )
        }

        Image(
            painter = rememberAsyncImagePainter(product.imageUrl),
            contentDescription = "Product Image",
            modifier = Modifier
                .height(300.dp)
                .width(300.dp)
                .padding(start = normal),
            contentScale = ContentScale.Inside
        )

        Text(
            text = product.name,
            maxLines = 1,
            modifier = Modifier.padding(vertical = normal),
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.h6.copy(
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        )

        RatingBar(rating = product.ratingBar)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = normal),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = product.price.toEuro(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.subtitle1.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = stringResource(
                    id = R.string.available_stock,
                    product.stock - product.itemAdded.value
                ),
                modifier = Modifier.padding(horizontal = normal),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.caption.copy(
                    color = if ((product.stock - product.itemAdded.value) != 0) Color.Black else Color.Red,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }

        Text(
            text = stringResource(id = R.string.about),
            modifier = Modifier.padding(vertical = normal),
            style = MaterialTheme.typography.body2.copy(
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )
        )

        Text(
            text = product.description,
            modifier = Modifier.padding(vertical = normal),
            style = MaterialTheme.typography.body2.copy(
                color = Color.Black,
                fontWeight = FontWeight.Light
            )
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = normal),
            onClick = {
                onClick(product)
                onClose()
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Purple700),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                text = stringResource(id = R.string.add_to_cart),
                modifier = Modifier.padding(vertical = normal),
                style = MaterialTheme.typography.subtitle1.copy(
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
            )
        }
    }
}
