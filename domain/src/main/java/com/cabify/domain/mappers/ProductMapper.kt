package com.cabify.domain.mappers

import com.cabify.data.db.ProductEntity
import com.cabify.data.response.product.ProductFieldsResponse
import com.cabify.domain.models.ProductModel

fun ProductFieldsResponse.toProductEntity() = ProductEntity(
    code = code,
    name = name,
    price = price
)

fun ProductFieldsResponse.toProductModel() = ProductModel(
    code = code,
    name = name,
    price = price
)

fun ProductEntity.toProductModel() = ProductModel(
    code = code,
    name = name,
    price = price
)