package com.imregulkurt.app_capstone.di

import com.imregulkurt.app_capstone.data.repository.ProductRepository
import com.imregulkurt.app_capstone.data.source.local.ProductDao
import com.imregulkurt.app_capstone.data.source.remote.ProductService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideProductRepository(productService: ProductService, productDao: ProductDao) =
        ProductRepository(productService, productDao)
}
