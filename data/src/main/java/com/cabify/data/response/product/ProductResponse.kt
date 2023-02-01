package com.cabify.data.response.product

data class ProductResponse(
    val products: List<ProductFieldsResponse>
)

data class ProductFieldsResponse(
    val code: String = "",
    val name: String = "",
    val price: Double = 0.0,
    var imageUrl: String = "",
    var description:String = "",
    var ratingBar:Double = 0.0,
    var stock:Int = 0
)
