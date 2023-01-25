package com.cabify.domain.di

import com.cabify.data.source.local.ProductLocalDataSourceImpl
import com.cabify.data.source.remote.ProductRemoteDataSourceImpl
import com.cabify.domain.repositories.ProductRepository
import com.cabify.domain.repositories.ProductRepositoryImpl
import com.cabify.domain.usecases.ProductUseCase
import com.cabify.domain.usecases.ProductUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ProductModule {
    @Provides
    fun providerRepository(
        remoteDataSource: ProductRemoteDataSourceImpl,
        localDataSource: ProductLocalDataSourceImpl
    ): ProductRepository = ProductRepositoryImpl(
        remoteDataSource = remoteDataSource,
        localDataSource = localDataSource
    )

    @Provides
    fun providerUseCase(repository: ProductRepository): ProductUseCase =
        ProductUseCaseImpl(repository)
}
