package com.cabify.domain.repositories

import com.cabify.common.Constants
import com.cabify.common.DomainErrorFactory
import com.cabify.common.Either
import com.cabify.common.ProductType
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

                    response.r.products.forEach {
                        when (it.code) {
                            ProductType.VOUCHER.code -> it.apply {
                                imageUrl = Constants.IMAGE_VOUCHER
                            }
                            ProductType.TSHIRT.code -> it.apply {
                                imageUrl = Constants.IMAGE_TSHIRT
                            }
                            ProductType.MUG.code -> it.apply { imageUrl = Constants.IMAGE_MUG }
                            else -> it.apply { it.apply { imageUrl = Constants.IMAGE_DEFAULT } }
                        }
                    }

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
