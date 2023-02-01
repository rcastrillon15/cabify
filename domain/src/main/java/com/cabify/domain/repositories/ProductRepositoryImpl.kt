package com.cabify.domain.repositories

import com.cabify.common.Constants
import com.cabify.common.Constants.MUG_DESCRIPTION
import com.cabify.common.Constants.TSHIRT_DESCRIPTION
import com.cabify.common.Constants.VOUCHER_DESCRIPTION
import com.cabify.common.DomainErrorFactory
import com.cabify.common.Either
import com.cabify.common.ProductType
import com.cabify.common.framework.NetworkMonitor
import com.cabify.data.response.product.ProductFieldsResponse
import com.cabify.data.source.local.ProductLocalDataSourceImpl
import com.cabify.data.source.remote.ProductRemoteDataSourceImpl
import com.cabify.domain.mappers.toProductEntity
import com.cabify.domain.mappers.toProductModel
import com.cabify.domain.mappers.toProductPayRequest
import com.cabify.domain.models.ProductModel
import com.cabify.domain.models.ProductPayModel
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: ProductRemoteDataSourceImpl,
    private val localDataSource: ProductLocalDataSourceImpl
) : ProductRepository {
    override suspend fun getProduct(): Either<DomainErrorFactory, List<ProductModel>> {
        if (NetworkMonitor().isConnected()) {
            return when (val response = remoteDataSource.getProduct()) {
                is Either.Right -> {

                    val recreatedData = fillData(response.r.products)

                    localDataSource.insert(recreatedData.map { it.toProductEntity() })
                    Either.Right(recreatedData.map { it.toProductModel() })
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

    override suspend fun pay(products: List<ProductPayModel>): Either<DomainErrorFactory, Boolean> {
        return if (NetworkMonitor().isConnected()) {
            when (val response = remoteDataSource.pay(products.map { it.toProductPayRequest() })) {
                is Either.Right -> {
                    Either.Right(response.r)
                }
                is Either.Left -> {
                    Either.Left(response.l)
                }
            }
        } else {
            Either.Right(false)
        }
    }

    private fun fillData(products: List<ProductFieldsResponse>): List<ProductFieldsResponse> {
        products.forEach {
            when (it.code) {
                ProductType.VOUCHER.code -> it.apply {
                    imageUrl = Constants.IMAGE_VOUCHER
                    description = VOUCHER_DESCRIPTION
                    stock = 15
                    ratingBar = 5.0
                }
                ProductType.TSHIRT.code -> it.apply {
                    imageUrl = Constants.IMAGE_TSHIRT
                    description = TSHIRT_DESCRIPTION
                    stock = 20
                    ratingBar = 5.0
                }
                ProductType.MUG.code -> it.apply {
                    imageUrl = Constants.IMAGE_MUG
                    description = MUG_DESCRIPTION
                    stock = 10
                    ratingBar = 5.0
                }
                else -> it.apply {
                    it.apply {
                        imageUrl = Constants.IMAGE_DEFAULT
                        description = ""
                        stock = 0
                        ratingBar = 0.0
                    }
                }
            }
        }
        return products
    }
}
