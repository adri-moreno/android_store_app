package com.androidstoreapp.domain

import com.androidstoreapp.domain.repository.ProductRepository
import com.androidstoreapp.domain.usecase.ToggleFavoriteUseCase
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ToggleFavoriteUseCaseTest {

    private val repo: ProductRepository = mockk(relaxed = true)
    private val useCase = ToggleFavoriteUseCase(repo)

    @Test
    fun `when isFavorite true, delegates to repo with true`() = runTest {
        useCase(productId = 1, isFavorite = true)
        coVerify(exactly = 1) { repo.toggleFavorite(1, true) }
    }

    @Test
    fun `when isFavorite false, delegates to repo with false`() = runTest {
        useCase(productId = 5, isFavorite = false)
        coVerify(exactly = 1) { repo.toggleFavorite(5, false) }
    }

    @Test
    fun `only toggles the requested productId, not others`() = runTest {
        useCase(productId = 3, isFavorite = true)
        coVerify(exactly = 0) { repo.toggleFavorite(not(eq(3)), any()) }
    }
}
