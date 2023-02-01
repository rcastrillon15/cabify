package com.cabify.data.source.remote

import com.cabify.common.Either
import com.cabify.data.response.product.ProductResponse

fun ProductResponse.toEiterRight() = Either.Right(this)