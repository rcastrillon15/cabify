package com.cabify.domain.usecases

import com.cabify.common.DomainErrorFactory
import com.cabify.common.Either
import com.cabify.domain.models.ProductModel

interface ProductUseCase {
    suspend fun getProduct(): Either<DomainErrorFactory, List<ProductModel>>
}
