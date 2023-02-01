package com.cabify.data.source.remote

import com.cabify.data.response.product.ProductFieldsResponse
import com.cabify.data.response.product.ProductResponse

object ProductRemoteDummy {
    fun productResponseDummy() = ProductResponse(
        listOf(
            ProductFieldsResponse(
                code = "VOUCHER",
                name = "Cabify Voucher",
                price = 5.0,
                imageUrl = "",
                description = "",
                ratingBar = 0.0,
                stock = 0
            ),
            ProductFieldsResponse(
                code = "TSHIRT",
                name = "Cabify T-Shirt",
                price = 20.0,
                imageUrl = "",
                description = "",
                ratingBar = 0.0,
                stock = 0
            ),
            ProductFieldsResponse(
                code = "MUG",
                name = "Cabify Coffee Mug",
                price = 7.5,
                imageUrl = "",
                description = "",
                ratingBar = 0.0,
                stock = 0
            )
        )
    )
}