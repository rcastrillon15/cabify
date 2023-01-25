package com.cabify.domain.repositories

import com.cabify.common.DomainErrorFactory
import com.cabify.common.Either
import com.cabify.domain.models.ProductModel

interface ProductRepository {
    suspend fun getProduct(): Either<DomainErrorFactory, List<ProductModel>>
}
