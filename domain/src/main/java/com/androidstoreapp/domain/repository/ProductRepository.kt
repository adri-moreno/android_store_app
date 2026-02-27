package com.androidstoreapp.domain.repository

import com.androidstoreapp.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun observeProducts(): Flow<Result<List<Product>>>
    suspend fun getProducts(): Result<List<Product>>
    fun observeFavoriteIds(): Flow<Set<Int>>
    suspend fun toggleFavorite(productId: Int, isFavorite: Boolean)
}
