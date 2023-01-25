package com.cabify.data.source.local

import com.cabify.common.DomainErrorFactory
import com.cabify.common.Either
import com.cabify.data.db.IProductLocal
import com.cabify.data.db.ProductEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductLocalDataSourceImpl @Inject constructor(private val local: IProductLocal) :
    ProductLocalDataSource {
    override suspend fun getProduct(): Either<DomainErrorFactory, List<ProductEntity>> =
        try {
            val response = withContext(Dispatchers.IO) { local.select() }

            when {
                response.isNotEmpty() -> {
                    Either.Right(response)
                }
                else -> {
                    Either.Left(DomainErrorFactory(errorCode = -2))
                }
            }
        } catch (exception: Exception) {
            Either.Left(DomainErrorFactory())
        }

    override suspend fun insert(productEntity: List<ProductEntity>) {
        local.transaction(productEntity)
    }
}
