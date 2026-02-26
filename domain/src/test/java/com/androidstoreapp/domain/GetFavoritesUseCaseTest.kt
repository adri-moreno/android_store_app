package com.androidstoreapp.domain

import com.androidstoreapp.domain.repository.ProductRepository
import com.androidstoreapp.domain.usecase.GetFavoritesUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import app.cash.turbine.test

class GetFavoritesUseCaseTest {

    private val repo: ProductRepository = mockk()
    private val useCase = GetFavoritesUseCase(repo)

    @Test
    fun `emits same set as repo observeFavoriteIds`() = runTest {
        val fakeIds = setOf(1, 2, 3)
        every { repo.observeFavoriteIds() } returns flowOf(fakeIds)

        useCase().test {
            assertEquals(fakeIds, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits empty set when no favorites`() = runTest {
        every { repo.observeFavoriteIds() } returns flowOf(emptySet())

        useCase().test {
            assertTrue(awaitItem().isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
