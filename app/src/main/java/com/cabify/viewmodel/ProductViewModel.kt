package com.cabify.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabify.common.LoadingViewState
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
}