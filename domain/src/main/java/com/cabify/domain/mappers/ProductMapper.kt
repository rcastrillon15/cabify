package com.cabify.domain.mappers

import com.cabify.data.db.ProductEntity
import com.cabify.data.request.product.ProductPayRequest
import com.cabify.data.response.product.ProductFieldsResponse
import com.cabify.domain.models.ProductModel
import com.cabify.domain.models.ProductPayModel

fun ProductFieldsResponse.toProductEntity() = ProductEntity(
    code = code,
    name = name,
    price = price,
    imageUrl = imageUrl,
    description = description,
    ratingBar = ratingBar,
    stock = stock
)

fun ProductFieldsResponse.toProductModel() = ProductModel(
    code = code,
    name = name,
    price = price,
    imageUrl = imageUrl,
    description = description,
    ratingBar = ratingBar,
    stock = stock
)

fun ProductEntity.toProductModel() = ProductModel(
    code = code,
    name = name,
    price = price,
    imageUrl = imageUrl,
    description = description,
    ratingBar = ratingBar,
    stock = stock
)

fun ProductPayModel.toProductPayRequest() = ProductPayRequest(
    code = code,
    quantity = quantity
)

fun ProductModel.toProductPayModel() = ProductPayModel(
    code = code,
    quantity = itemAdded.value
)