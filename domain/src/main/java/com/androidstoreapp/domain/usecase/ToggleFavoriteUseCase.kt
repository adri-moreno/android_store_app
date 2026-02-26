package com.androidstoreapp.domain.usecase

import com.androidstoreapp.domain.repository.ProductRepository

class ToggleFavoriteUseCase(private val repo: ProductRepository) {
    suspend operator fun invoke(productId: Int, isFavorite: Boolean) =
        repo.toggleFavorite(productId, isFavorite)
}
