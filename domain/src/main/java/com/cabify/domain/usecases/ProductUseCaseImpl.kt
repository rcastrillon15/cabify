package com.cabify.domain.usecases

import com.cabify.common.DomainErrorFactory
import com.cabify.common.Either
import com.cabify.domain.models.ProductModel
import com.cabify.domain.models.ProductPayModel
import com.cabify.domain.repositories.ProductRepository
import javax.inject.Inject

class ProductUseCaseImpl @Inject constructor(private val repository: ProductRepository) :
    ProductUseCase {
    override suspend fun getProduct(): Either<DomainErrorFactory, List<ProductModel>> =
        repository.getProduct()

    override suspend fun pay(products: List<ProductPayModel>): Either<DomainErrorFactory, Boolean> =
        repository.pay(products)
}
