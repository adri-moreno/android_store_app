package com.androidstoreapp.feature.favorites

import com.androidstoreapp.core.ui.UiState
import com.androidstoreapp.domain.model.Product
import com.androidstoreapp.domain.usecase.GetFavoritesUseCase
import com.androidstoreapp.domain.usecase.GetProductsUseCase
import com.androidstoreapp.domain.usecase.ToggleFavoriteUseCase
import com.androidstoreapp.domain.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModelTest {

    @get:Rule val mainRule = MainDispatcherRule()

    private val getProducts: GetProductsUseCase = mockk()
    private val getFavorites: GetFavoritesUseCase = mockk()
    private val toggle: ToggleFavoriteUseCase = mockk(relaxed = true)

    @Test
    fun `only favorite products appear in Content`() = runTest {
        val all = listOf(Product(1, "A", 10.0, ""), Product(2, "B", 20.0, ""))
        coEvery { getProducts() } returns Result.success(all)
        every { getFavorites() } returns flowOf(setOf(2))

        val vm = FavoritesViewModel(getProducts, getFavorites, toggle)
        val content = vm.uiState.value as UiState.Content
        assertEquals(1, content.data.size)
        assertEquals(2, content.data.first().id)
    }

    @Test
    fun `all favorite products have isFavorite true`() = runTest {
        val all = listOf(Product(1, "A", 10.0, ""), Product(2, "B", 20.0, ""))
        coEvery { getProducts() } returns Result.success(all)
        every { getFavorites() } returns flowOf(setOf(1, 2))

        val vm = FavoritesViewModel(getProducts, getFavorites, toggle)
        val content = vm.uiState.value as UiState.Content
        assertTrue(content.data.all { it.isFavorite })
    }

    @Test
    fun `when no favorites, Content data is empty`() = runTest {
        coEvery { getProducts() } returns Result.success(listOf(Product(1, "A", 10.0, "")))
        every { getFavorites() } returns flowOf(emptySet())

        val vm = FavoritesViewModel(getProducts, getFavorites, toggle)
        assertTrue((vm.uiState.value as UiState.Content).data.isEmpty())
    }

    @Test
    fun `when api fails, emits Error`() = runTest {
        coEvery { getProducts() } returns Result.failure(Exception("Timeout"))
        every { getFavorites() } returns flowOf(emptySet())

        val vm = FavoritesViewModel(getProducts, getFavorites, toggle)
        assertTrue(vm.uiState.value is UiState.Error)
    }
}
