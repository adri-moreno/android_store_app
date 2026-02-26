package com.androidstoreapp.core.database

import com.androidstoreapp.core.database.dao.FavoriteDao
import com.androidstoreapp.core.database.entity.FavoriteEntity
import com.androidstoreapp.domain.datasource.FavoriteLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteLocalDataSourceImpl(
    private val dao: FavoriteDao
) : FavoriteLocalDataSource {

    override fun observeAll(): Flow<List<Int>> =
        dao.observeAll().map { list -> list.map { it.productId } }

    override suspend fun insert(productId: Int) =
        dao.insert(FavoriteEntity(productId))

    override suspend fun delete(productId: Int) =
        dao.delete(FavoriteEntity(productId))
}
