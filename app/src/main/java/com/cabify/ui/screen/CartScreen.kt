package com.cabify.ui.screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.cabify.R
import com.cabify.common.LoadState
import com.cabify.common.toEuro
import com.cabify.components.BackArrow
import com.cabify.components.CreditCard
import com.cabify.components.LinearProgressBarCustom
import com.cabify.components.LoadErrorScreen
import com.cabify.domain.models.ProductModel
import com.cabify.ui.theme.Gray_FFF8F8F8
import com.cabify.ui.theme.Purple700
import com.cabify.ui.theme.medium
import com.cabify.ui.theme.normal
import com.cabify.ui.theme.regular
import com.cabify.ui.theme.small
import com.cabify.ui.theme.xsmall
import com.cabify.ui.theme.xxlarge
import com.cabify.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CartScreen(
    onNavigate: () -> Unit,
    onBack: () -> Unit,
    viewModel: ProductViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val productState by viewModel.productState.collectAsState()
    val productPayState by viewModel.productPayState.collectAsState()
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    var cardNumber by remember { mutableStateOf("8547 9658 6325 4521") }
    var cardHolder by remember { mutableStateOf("RODRIGO CASTRILLON") }
    var cardExpiration by remember { mutableStateOf("02/25") }
    var cardCVV by remember { mutableStateOf("123") }

    BackHandler(bottomSheetState.isVisible) {
        scope.launch {
            bottomSheetState.hide()
        }
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .padding(medium)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {

                IconButton(
                    onClick = { scope.launch { bottomSheetState.hide() } },
                    modifier = Modifier.padding(vertical = xsmall, horizontal = small)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Purple700
                    )
                }

                CreditCard(
                    cardNumber = cardNumber,
                    cardHolder = cardHolder,
                    cardExpiration = cardExpiration
                )

                TextField(
                    value = cardNumber,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    onValueChange = {
                        if (it.length <= 19) cardNumber = it
                    },
                    label = { Text("Card number") },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White
                    )
                )

                TextField(
                    value = cardHolder.uppercase(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    onValueChange = {
                        if (it.length <= 25) cardHolder = it
                    },
                    label = { Text("Card holder") },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters)
                )

                TextField(
                    value = cardExpiration,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    onValueChange = {
                        if (it.length <= 5) cardExpiration = it
                    },
                    label = { Text("Card expiration") },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White
                    )
                )

                TextField(
                    value = cardCVV,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    onValueChange = {
                        if (it.length <= 3) cardCVV = it
                    },
                    label = { Text("Card cvv") },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White
                    )
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = medium),
                    onClick = viewModel::checkout,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Purple700),
                    shape = RoundedCornerShape(28.dp)
                ) {

                    Text(
                        text = stringResource(
                            id = R.string.total_with_discount,
                            viewModel.totalToPay.value.toEuro()
                        ),
                        style = MaterialTheme.typography.subtitle2.copy(
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        ),
                        modifier = Modifier.padding(vertical = normal)
                    )
                }
            }
        }) {

        Scaffold(
            topBar = {
                TopAppBar(
                    backgroundColor = Color.White,
                    contentColor = Purple700,
                    title = { Text(stringResource(R.string.shopping_cart)) },
                    navigationIcon = { BackArrow(onBack) },
                )
            },
            floatingActionButton = {
                Button(
                    enabled = viewModel.countAddedProducts.value != 0,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = normal),
                    onClick = {
                        scope.launch {
                            bottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Purple700),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.total_without_discount,
                                viewModel.totalToPay.value.toEuro()
                            ),
                            style = MaterialTheme.typography.caption.copy(
                                textAlign = TextAlign.Center,
                                color = Color.Gray,
                                fontWeight = FontWeight.SemiBold,
                                textDecoration = TextDecoration.LineThrough
                            )
                        )

                        Text(
                            text = stringResource(
                                id = R.string.total_with_discount,
                                viewModel.totalToPay.value.toEuro()
                            ),
                            style = MaterialTheme.typography.subtitle2.copy(
                                textAlign = TextAlign.Center,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.Center
        ) { paddingValues ->
            Column(Modifier.padding(paddingValues)) {
                if (viewModel.countAddedProducts.value != 0) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = xxlarge),
                        state = rememberLazyListState()
                    ) {
                        itemsIndexed(productState.data) { _, product ->
                            if (product.itemAdded.value != 0) {
                                ItemAddedProduct(
                                    product = product,
                                    onAdd = {
                                        viewModel.addProduct(it)
                                    }, onDelete = {
                                        viewModel.deleteProduct(it)
                                    })
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.empty_cart),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(normal),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.h5.copy(
                                color = Color.Black,
                                fontWeight = FontWeight.Light,
                                textAlign = TextAlign.Center
                            )
                        )

                        ClickableText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(normal),
                            text = buildAnnotatedString { append(stringResource(id = R.string.see_products)) },
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.h6.copy(
                                color = Purple700,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            ),
                            onClick = { onBack() }
                        )
                    }
                }
            }

            if (viewModel.showMessage.value) {
                Toast.makeText(context, stringResource(id = R.string.no_stock), Toast.LENGTH_LONG)
                    .show()
                viewModel.showMessage.value = false
            }

            when (productPayState) {
                is LoadState.Failure -> LoadErrorScreen(viewModel.stateErrorMessage)
                is LoadState.IsLoading -> LinearProgressBarCustom()
                is LoadState.Success -> {

                    //onNavigate()
                }
                else -> {

                }
            }
        }
    }
}

@Composable
fun ItemAddedProduct(
    product: ProductModel,
    onAdd: (addProduct: ProductModel) -> Unit,
    onDelete: (deleteProduct: ProductModel) -> Unit
) {
    Card(
        shape = RoundedCornerShape(normal),
        modifier = Modifier
            .fillMaxWidth()
            .padding(normal)
            .padding(horizontal = regular, vertical = small)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Gray_FFF8F8F8)
        ) {

            Text(
                text = product.name,
                modifier = Modifier
                    .padding(horizontal = normal)
                    .fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h6.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            )

            Row {
                Image(
                    painter = rememberAsyncImagePainter(product.imageUrl),
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .height(130.dp)
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
                            .padding(start = normal, top = medium, end = normal)
                            .align(Alignment.End),
                        style = MaterialTheme.typography.subtitle2.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    elevation = 0.dp,
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier.padding(start = normal, bottom = normal)
                ) {
                    Row(
                        modifier = Modifier
                            .background(Color.White),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        IconButton(
                            onClick = { onDelete(product) },
                            modifier = Modifier
                                .padding(vertical = xsmall, horizontal = small)
                        ) {
                            Icon(
                                painterResource(id = R.drawable.ic_delete),
                                contentDescription = "Delete",
                                tint = Color.Red
                            )
                        }

                        Text(
                            text = product.itemAdded.value.toString(),
                            style = MaterialTheme.typography.subtitle2.copy(
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.padding(vertical = xsmall, horizontal = small)
                        )

                        IconButton(
                            onClick = { onAdd(product) },
                            modifier = Modifier.padding(vertical = xsmall, horizontal = small)
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add",
                                tint = Purple700
                            )
                        }
                    }
                }

                Text(
                    text = stringResource(id = R.string.discount, "4500€"),
                    style = MaterialTheme.typography.caption.copy(
                        color = Purple700,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = normal)
                )
            }
        }
    }
}