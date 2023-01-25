package com.cabify.domain.repositories

import com.cabify.common.DomainErrorFactory
import com.cabify.common.Either
import com.cabify.common.framework.NetworkMonitor
import com.cabify.data.source.local.ProductLocalDataSourceImpl
import com.cabify.data.source.remote.ProductRemoteDataSourceImpl
import com.cabify.domain.mappers.toProductEntity
import com.cabify.domain.mappers.toProductModel
import com.cabify.domain.models.ProductModel
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: ProductRemoteDataSourceImpl,
    private val localDataSource: ProductLocalDataSourceImpl
) : ProductRepository {
    override suspend fun getProduct(): Either<DomainErrorFactory, List<ProductModel>> {
        if (NetworkMonitor().isConnected()) {
            return when (val response = remoteDataSource.getProduct()) {
                is Either.Right -> {
                    localDataSource.insert(response.r.products.map { it.toProductEntity() })
                    Either.Right(response.r.products.map { it.toProductModel() })
                }
                is Either.Left -> {
                    Either.Left(response.l)
                }
            }
        } else {
            return when (val response = localDataSource.getProduct()) {
                is Either.Right -> {
                    Either.Right(response.r.map { it.toProductModel() })
                }

                is Either.Left -> {
                    Either.Left(response.l)
                }
            }
        }
    }
}
