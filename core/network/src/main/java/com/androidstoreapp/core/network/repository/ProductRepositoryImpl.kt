package com.androidstoreapp.core.network.repository

import com.androidstoreapp.core.network.ApiService
import com.androidstoreapp.core.network.mapper.toDomain
import com.androidstoreapp.domain.datasource.FavoriteLocalDataSource
import com.androidstoreapp.domain.model.Product
import com.androidstoreapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl(
    private val api: ApiService,
    private val favoriteDataSource: FavoriteLocalDataSource
) : ProductRepository {

    override suspend fun getProducts(): Result<List<Product>> =
        runCatching { api.getProducts().map { it.toDomain() } }

    override fun observeFavoriteIds(): Flow<Set<Int>> =
        favoriteDataSource.observeAll().map { it.toSet() }

    override suspend fun toggleFavorite(productId: Int, isFavorite: Boolean) {
        if (isFavorite) favoriteDataSource.insert(productId)
        else favoriteDataSource.delete(productId)
    }
}
