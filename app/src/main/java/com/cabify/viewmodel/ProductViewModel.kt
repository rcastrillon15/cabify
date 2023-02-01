package com.cabify.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabify.common.LoadState
import com.cabify.common.LoadingViewState
import com.cabify.common.ProductType
import com.cabify.domain.mappers.toProductPayModel
import com.cabify.domain.models.ProductModel
import com.cabify.domain.usecases.ProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val useCase: ProductUseCase
) : ViewModel() {

    var stateErrorMessage by mutableStateOf("")
        private set

    private val _productState = MutableStateFlow(LoadingViewState<List<ProductModel>>(emptyList()))
    val productState = _productState.asStateFlow()

    private val _productPayState = MutableSharedFlow<LoadState>()
    val productPayState = _productPayState.asSharedFlow()

    var countAddedProducts = mutableStateOf(0)

    var showMessage = mutableStateOf(false)

    var totalToPay = mutableStateOf(0.0)

    init {
        getProduct()
    }

    private fun getProduct() {
        viewModelScope.launch {
            val newState = useCase.getProduct()
                .fold({
                    stateErrorMessage = it.toString()
                    _productState.value.asFailure()
                }, { model ->
                    _productState.value.asSuccess(model.map { it })
                })
            _productState.update { newState }
        }
    }

    fun updateProduct(product: ProductModel) {
        _productState.update {
            it.apply {
                this.data.apply {
                    this.single { value -> value.code == product.code }
                        .apply {
                            when (product.code) {
                                ProductType.VOUCHER.code -> {
                                    when {
                                        product.itemAdded.value % 2 == 0 -> totalToPay.value += ((product.itemAdded.value % 2))
                                        else -> {
                                            when {
                                                product.itemAdded.value / 2 != 0 -> totalToPay.value += (product.itemAdded.value % 2) * product.price
                                                else -> totalToPay.value += product.price
                                            }
                                        }
                                    }
                                }
                                ProductType.TSHIRT.code -> {

                                    when {
                                        product.itemAdded.value >= 3 -> {
                                            when (product.itemAdded.value) {
                                                3 -> totalToPay.value += (product.price - product.itemAdded.value)
                                                else -> totalToPay.value += (product.price - 1)
                                            }
                                        }
                                        else -> totalToPay.value += product.price
                                    }
                                }
                                ProductType.MUG.code -> totalToPay.value += product.price
                            }
                        }
                }
            }
        }
        countAddedProducts.value = _productState.value.data.sumOf { it.itemAdded.value }
    }

    fun subtractPrice(productParam: ProductModel) {
        _productState.update {
            it.apply {
                this.data.onEach { product ->
                    if (product.code == productParam.code) {
                        when (product.code) {
                            ProductType.VOUCHER.code -> {
                                when {
                                    product.itemAdded.value % 2 == 0 -> {
                                        when {
                                            product.itemAdded.value != 0 ->
                                                totalToPay.value -= ((product.itemAdded.value % 2))
                                            else ->
                                                totalToPay.value -= product.price
                                        }
                                    }

                                    product.itemAdded.value == 1 -> totalToPay.value -= product.price

                                    else -> {
                                        when {
                                            product.itemAdded.value / 2 != 0 -> totalToPay.value -= (product.itemAdded.value % 2) * product.price
                                            else -> totalToPay.value -= product.price
                                        }
                                    }
                                }
                            }

                            ProductType.TSHIRT.code -> {
                                when {
                                    product.itemAdded.value >= 3 -> totalToPay.value -= (product.price - 1)
                                    product.itemAdded.value == 2 -> totalToPay.value =
                                        totalToPay.value - (product.price) + (product.itemAdded.value) + 1
                                    else -> totalToPay.value -= product.price
                                }
                            }

                            ProductType.MUG.code -> totalToPay.value -= product.price
                        }
                    }
                }
            }
        }
        countAddedProducts.value = _productState.value.data.sumOf { it.itemAdded.value }
    }

    fun validateStock(product: ProductModel, inStock: (Boolean) -> Unit) {
        _productState.update {
            it.apply {
                this.data.apply {
                    this.single { value -> value.code == product.code }
                        .apply {
                            if (product.itemAdded.value < stock) {
                                inStock(true)
                            } else {
                                showMessage.value = true
                                inStock(!showMessage.value)
                            }
                        }
                }
            }
        }
    }

    fun cleanCart() {
        _productState.update { state ->
            state.apply {
                this.data.apply {
                    this.map { it.apply { itemAdded.value = 0 } }
                }
            }
        }

        totalToPay.value = 0.0
        countAddedProducts.value = _productState.value.data.sumOf { it.itemAdded.value }
    }

    fun checkout() {
        viewModelScope.launch {
            _productPayState.emit(LoadState.Loading)
            useCase.pay(_productState.value.data.map { it.toProductPayModel() })
                .fold({
                    stateErrorMessage = it.toString()
                    viewModelScope.launch {
                        _productPayState.emit(LoadState.Error)
                    }
                }, {
                    viewModelScope.launch {
                        _productPayState.emit(LoadState.Success)
                    }
                })
        }
    }
}
