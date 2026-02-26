package com.androidstoreapp.domain.usecase

import com.androidstoreapp.domain.model.Product
import com.androidstoreapp.domain.repository.ProductRepository

class GetProductsUseCase(private val repo: ProductRepository) {
    suspend operator fun invoke(): Result<List<Product>> = repo.getProducts()
}
