package com.cabify.data.source.remote

import com.cabify.common.DomainErrorFactory
import com.cabify.common.Either
import com.cabify.data.response.product.ProductResponse

interface ProductRemoteDataSource {
    suspend fun getProduct(): Either<DomainErrorFactory, ProductResponse>
}
