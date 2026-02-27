package com.androidstoreapp.core.network.repository

import com.androidstoreapp.core.network.ApiService
import com.androidstoreapp.core.network.NetworkMonitor
import com.androidstoreapp.core.network.mapper.mapErrorToDomain
import com.androidstoreapp.core.network.mapper.toDomain
import com.androidstoreapp.domain.datasource.FavoriteLocalDataSource
import com.androidstoreapp.domain.model.Product
import com.androidstoreapp.domain.repository.ProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
class ProductRepositoryImpl(
    private val api: ApiService,
    private val favoriteDataSource: FavoriteLocalDataSource,
    private val networkMonitor: NetworkMonitor
) : ProductRepository {

    private var cachedProducts: List<Product>? = null

    override fun observeProducts(): Flow<Result<List<Product>>> = 
        networkMonitor.isOnline
            .distinctUntilChanged()
            .flatMapLatest { isOnline ->
                flow {
                    if (isOnline || cachedProducts == null) {
                        emit(getProducts())
                    } else {
                        emit(Result.success(cachedProducts!!))
                    }
                }
            }

    override suspend fun getProducts(): Result<List<Product>> =
        runCatching {
            api.getProducts().map { it.toDomain() }.also { cachedProducts = it }
        }.recoverCatching {
            cachedProducts ?: throw it
        }.mapErrorToDomain()

    override fun observeFavoriteIds(): Flow<Set<Int>> =
        favoriteDataSource.observeAll().map { it.toSet() }

    override suspend fun toggleFavorite(productId: Int, isFavorite: Boolean) {
        if (isFavorite) favoriteDataSource.insert(productId)
        else favoriteDataSource.delete(productId)
    }
}
