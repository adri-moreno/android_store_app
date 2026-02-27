package com.androidstoreapp.core.network

import app.cash.turbine.test
import com.androidstoreapp.core.network.dto.ProductDto
import com.androidstoreapp.core.network.dto.RatingDto
import com.androidstoreapp.core.network.repository.ProductRepositoryImpl
import com.androidstoreapp.domain.datasource.FavoriteLocalDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.IOException

class ProductRepositoryImplTest {

    private val api: ApiService = mockk()
    private val favoriteDataSource: FavoriteLocalDataSource = mockk(relaxed = true)
    private val networkMonitor: NetworkMonitor = mockk(relaxed = true)
    private val repo = ProductRepositoryImpl(api, favoriteDataSource, networkMonitor)

    @Test
    fun `getProducts maps DTOs to domain models correctly`() = runTest {
        val dto = ProductDto(1, "Shirt", 19.99, "desc", "clothing", "img", RatingDto(4.5, 200))
        coEvery { api.getProducts() } returns listOf(dto)

        val result = repo.getProducts()

        assertTrue(result.isSuccess)
        with(result.getOrNull()!!.first()) {
            assertEquals(1, id)
            assertEquals("Shirt", title)
            assertEquals(19.99, price, 0.001)
            assertEquals("img", image)
            assertEquals("desc", description)
        }
    }

    @Test
    fun `getProducts returns failure when api throws IOException and no cache`() = runTest {
        coEvery { api.getProducts() } throws IOException("No connection")

        assertTrue(repo.getProducts().isFailure)
    }

    @Test
    fun `getProducts returns cached products when api fails after a successful call`() = runTest {
        val dto = ProductDto(1, "Shirt", 19.99, "desc", "clothing", "img", RatingDto(4.5, 200))
        coEvery { api.getProducts() } returns listOf(dto)
        repo.getProducts()

        coEvery { api.getProducts() } throws IOException("No connection")
        val result = repo.getProducts()

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()!!.first().id)
    }

    @Test
    fun `toggleFavorite true inserts into data source`() = runTest {
        repo.toggleFavorite(productId = 7, isFavorite = true)

        coVerify(exactly = 1) { favoriteDataSource.insert(7) }
        coVerify(exactly = 0) { favoriteDataSource.delete(any()) }
    }

    @Test
    fun `toggleFavorite false deletes from data source`() = runTest {
        repo.toggleFavorite(productId = 7, isFavorite = false)

        coVerify(exactly = 1) { favoriteDataSource.delete(7) }
        coVerify(exactly = 0) { favoriteDataSource.insert(any()) }
    }

    @Test
    fun `observeFavoriteIds maps list to id set`() = runTest {
        every { favoriteDataSource.observeAll() } returns flowOf(listOf(1, 3))

        repo.observeFavoriteIds().test {
            assertEquals(setOf(1, 3), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `observeFavoriteIds emits empty set when data source has no entries`() = runTest {
        every { favoriteDataSource.observeAll() } returns flowOf(emptyList())

        repo.observeFavoriteIds().test {
            assertTrue(awaitItem().isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
