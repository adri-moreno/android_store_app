package com.androidstoreapp.domain.usecase

import com.androidstoreapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetFavoritesUseCase(private val repo: ProductRepository) {
    operator fun invoke(): Flow<Set<Int>> = repo.observeFavoriteIds()
}
