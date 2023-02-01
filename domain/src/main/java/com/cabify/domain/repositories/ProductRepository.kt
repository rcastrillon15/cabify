package com.cabify.domain.repositories

import com.cabify.common.DomainErrorFactory
import com.cabify.common.Either
import com.cabify.data.request.product.ProductPayRequest
import com.cabify.domain.models.ProductModel
import com.cabify.domain.models.ProductPayModel

interface ProductRepository {
    suspend fun getProduct(): Either<DomainErrorFactory, List<ProductModel>>
    suspend fun pay(products: List<ProductPayModel>): Either<DomainErrorFactory, Boolean>
}
