package com.cabify.data.source.remote

import com.cabify.common.DomainErrorFactory
import com.cabify.common.Either
import com.cabify.data.apiservice.IProductService
import com.cabify.data.request.product.ProductPayRequest
import com.cabify.data.response.product.ProductResponse
import javax.inject.Inject
import kotlinx.coroutines.delay

/**
 * Test class [ProductRemoteDataSourceImplTest]
 */
class ProductRemoteDataSourceImpl @Inject constructor(private val service: IProductService) :
    ProductRemoteDataSource {
    override suspend fun getProduct(): Either<DomainErrorFactory, ProductResponse> =
        try {
            val response = service.getProduct()
            when {
                response.isSuccessful -> {
                    Either.Right(checkNotNull(response.body()))
                }
                else -> {
                    Either.Left(DomainErrorFactory(response.code()))
                }
            }
        } catch (exception: Exception) {
            Either.Left(DomainErrorFactory(errorCode = exception.hashCode()))
        }

    override suspend fun pay(products: List<ProductPayRequest>): Either<DomainErrorFactory, Boolean> =
        try {
            delay(3000)
            Either.Right(checkNotNull(true))
        } catch (exception: Exception) {
            Either.Left(DomainErrorFactory(errorCode = exception.hashCode()))
        }
}
