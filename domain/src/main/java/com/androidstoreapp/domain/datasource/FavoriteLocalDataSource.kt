package com.androidstoreapp.domain.datasource

import kotlinx.coroutines.flow.Flow

interface FavoriteLocalDataSource {
    fun observeAll(): Flow<List<Int>>
    suspend fun insert(productId: Int)
    suspend fun delete(productId: Int)
}
