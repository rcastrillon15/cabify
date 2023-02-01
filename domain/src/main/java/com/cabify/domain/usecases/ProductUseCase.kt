package com.cabify.domain.usecases

import com.cabify.common.DomainErrorFactory
import com.cabify.common.Either
import com.cabify.domain.models.ProductModel
import com.cabify.domain.models.ProductPayModel

interface ProductUseCase {
    suspend fun getProduct(): Either<DomainErrorFactory, List<ProductModel>>

    suspend fun pay(products: List<ProductPayModel>): Either<DomainErrorFactory, Boolean>

}
