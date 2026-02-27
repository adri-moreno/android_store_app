package com.androidstoreapp.domain.usecase

import com.androidstoreapp.domain.model.Product
import com.androidstoreapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ObserveProductsUseCase(
    private val repository: ProductRepository
) {
    operator fun invoke(onlyFavorites: Boolean = false): Flow<Result<List<Product>>> = combine(
        repository.observeProducts(),
        repository.observeFavoriteIds()
    ) { result, favorites ->
        result.map { products ->
            val mappedProducts = products.map { product ->
                product.copy(isFavorite = product.id in favorites)
            }
            if (onlyFavorites) {
                mappedProducts.filter { it.isFavorite }
            } else {
                mappedProducts
            }
        }
    }
}
