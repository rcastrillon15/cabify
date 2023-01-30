package com.cabify.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabify.common.LoadState
import com.cabify.common.LoadingViewState
import com.cabify.domain.models.ProductModel
import com.cabify.domain.models.ProductPayModel
import com.cabify.domain.usecases.ProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProductViewModel @Inject constructor(
    //savedStateHandle: SavedStateHandle,
    private val useCase: ProductUseCase
) : ViewModel() {

    var stateErrorMessage by mutableStateOf("")
        private set

    private val _productState = MutableStateFlow(LoadingViewState<List<ProductModel>>(emptyList()))
    val productState = _productState.asStateFlow()

    private val _productPayState = MutableStateFlow<LoadState<Boolean>>(LoadState.InFlight)
    val productPayState = _productPayState.asStateFlow()

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

    fun addProduct(product: ProductModel) {
        _productState.update {
            it.apply {
                this.data.apply {
                    this.single { value -> value.code == product.code }.apply {
                        if (itemAdded.value < stock) {
                            itemAdded.value += 1
                        } else {
                            showMessage.value = true
                        }
                    }
                }
            }
        }
        countProducts()
        total()
    }

    fun deleteProduct(product: ProductModel) {
        _productState.update {
            it.apply {
                this.data.apply {
                    this.single { value -> value.code == product.code }
                        .apply { itemAdded.value -= 1 }
                }
            }
        }
        countProducts()
        total()
    }

    private fun countProducts() {
        countAddedProducts.value = _productState.value.data.sumOf { it.itemAdded.value }
    }

    private fun total() {

        totalToPay.value = _productState.value.data.sumOf { it.price }
    }

    fun checkout() {
        _productPayState.update { LoadState.IsLoading(true) }
        viewModelScope.launch {
            useCase.pay(listOf(ProductPayModel(code = "MUG")))
                .fold({
                    stateErrorMessage = it.toString()
                    _productPayState.update { LoadState.Failure }
                }, { value ->
                    _productPayState.update { LoadState.Success(value) }
                })
        }
    }
}
