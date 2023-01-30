package com.cabify.domain.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class ProductModel(
    val code: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val description:String = "",
    val ratingBar:Double = 0.0,
    val stock:Int = 0,
    var itemAdded: MutableState<Int> = mutableStateOf(0)
)
