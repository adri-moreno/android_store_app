package com.androidstoreapp.feature.favorites

import com.androidstoreapp.core.ui.UiState
import com.androidstoreapp.domain.model.Product
import com.androidstoreapp.domain.usecase.ObserveProductsUseCase
import com.androidstoreapp.domain.usecase.ToggleFavoriteUseCase
import com.androidstoreapp.domain.util.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModelTest {

    @get:Rule val mainRule = MainDispatcherRule()

    private val observeProducts: ObserveProductsUseCase = mockk()
    private val toggle: ToggleFavoriteUseCase = mockk(relaxed = true)
    
    private fun buildVm() = FavoritesViewModel(observeProducts, toggle)

    @Test
    fun `only favorite products appear in Content`() = runTest {
        val onlyFavorites = listOf(Product(2, "B", 20.0, "", isFavorite = true))
        every { observeProducts(onlyFavorites = true) } returns flowOf(Result.success(onlyFavorites))

        val vm = buildVm()
        val content = vm.uiState.value as UiState.Content
        assertEquals(1, content.data.size)
        assertEquals(2, content.data.first().id)
        assertTrue(content.data.first().isFavorite)
    }

    @Test
    fun `when no favorites, Content data is empty`() = runTest {
        every { observeProducts(onlyFavorites = true) } returns flowOf(Result.success(emptyList()))

        val vm = buildVm()
        assertTrue((vm.uiState.value as UiState.Content).data.isEmpty())
    }

    @Test
    fun `when api fails, emits Error`() = runTest {
        val exception = Exception("Timeout")
        every { observeProducts(onlyFavorites = true) } returns flowOf(Result.failure(exception))

        val vm = buildVm()
        val state = vm.uiState.value
        assertTrue("Expected Error but got $state", state is UiState.Error)
        assertEquals(exception, (state as UiState.Error).throwable)
    }
}
