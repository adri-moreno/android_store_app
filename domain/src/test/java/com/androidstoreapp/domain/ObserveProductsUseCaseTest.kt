package com.androidstoreapp.domain

import com.androidstoreapp.domain.model.Product
import com.androidstoreapp.domain.repository.ProductRepository
import com.androidstoreapp.domain.usecase.ObserveProductsUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ObserveProductsUseCaseTest {

    private val repo: ProductRepository = mockk()
    private val useCase = ObserveProductsUseCase(repo)

    @Test
    fun `maps products with favorite status correctly`() = runTest {
        val products = listOf(
            Product(1, "A", 10.0, ""),
            Product(2, "B", 20.0, "")
        )
        every { repo.observeProducts() } returns flowOf(Result.success(products))
        every { repo.observeFavoriteIds() } returns flowOf(setOf(1))

        val result = useCase().first()
        
        val list = result.getOrNull()!!
        assertTrue(list.first { it.id == 1 }.isFavorite)
        assertFalse(list.first { it.id == 2 }.isFavorite)
    }

    @Test
    fun `filters only favorites when onlyFavorites is true`() = runTest {
        val products = listOf(
            Product(1, "A", 10.0, ""),
            Product(2, "B", 20.0, "")
        )
        every { repo.observeProducts() } returns flowOf(Result.success(products))
        every { repo.observeFavoriteIds() } returns flowOf(setOf(2))

        val result = useCase(onlyFavorites = true).first()
        
        val list = result.getOrNull()!!
        assertEquals(1, list.size)
        assertEquals(2, list.first().id)
        assertTrue(list.first().isFavorite)
    }

    @Test
    fun `returns failure when getProducts fails`() = runTest {
        val exception = Exception("Network error")
        every { repo.observeProducts() } returns flowOf(Result.failure(exception))
        every { repo.observeFavoriteIds() } returns flowOf(emptySet())

        val result = useCase().first()
        
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
