package com.cabify.data.response.product

data class ProductResponse(
    val products: List<ProductFieldsResponse>
)

data class ProductFieldsResponse(
    val code: String,
    val name: String,
    val price: Double
)
