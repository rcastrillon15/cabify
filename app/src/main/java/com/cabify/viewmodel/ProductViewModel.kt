package com.cabify.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabify.common.Constants.IMAGE_DEFAULT
import com.cabify.common.Constants.IMAGE_MUG
import com.cabify.common.Constants.IMAGE_TSHIRT
import com.cabify.common.Constants.IMAGE_VOUCHER
import com.cabify.common.LoadingViewState
import com.cabify.common.ProductType
import com.cabify.domain.models.ProductModel
import com.cabify.domain.usecases.ProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCase: ProductUseCase
) : ViewModel() {

    var stateErrorMessage by mutableStateOf("")
        private set

    private val _productState = MutableStateFlow(LoadingViewState<List<ProductModel>>(emptyList()))
    val productState = _productState.asStateFlow()

    private val _selectedProductsState = MutableStateFlow(listOf(ProductModel()))
    val selectedProductsState = _selectedProductsState.asStateFlow()

    var countAddedProducts = mutableStateOf(0)

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

        _selectedProductsState.update {
            it.toMutableList().apply {
                add(product)
            }
        }
        countAddedProducts.value = _selectedProductsState.value.count()
    }

    fun deleteProduct(product: ProductModel) {

        _selectedProductsState.update {
            it.toMutableList().apply {
                this.forEachIndexed { index, value ->
                    if (value.code == product.code) {
                        removeAt(index)
                        return
                    }
                }
            }
        }

        countAddedProducts.value = _selectedProductsState.value.count()
    }

    fun checkout(){

    }
}
