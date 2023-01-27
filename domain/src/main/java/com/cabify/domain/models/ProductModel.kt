package com.cabify.domain.models

data class ProductModel(
    val code: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val description:String = "",
    val ratingBar:Double = 0.0,
    val stock:Int = 0
)
