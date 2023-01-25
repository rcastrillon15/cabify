package com.cabify.data.source.local

import com.cabify.common.DomainErrorFactory
import com.cabify.common.Either
import com.cabify.data.db.ProductEntity

interface ProductLocalDataSource {
    suspend fun getProduct(): Either<DomainErrorFactory, List<ProductEntity>>

    suspend fun insert(productEntity: List<ProductEntity>)
}
