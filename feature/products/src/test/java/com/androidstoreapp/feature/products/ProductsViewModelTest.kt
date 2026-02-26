package com.androidstoreapp.feature.products

import com.androidstoreapp.core.ui.UiState
import com.androidstoreapp.domain.model.Product
import com.androidstoreapp.domain.usecase.GetFavoritesUseCase
import com.androidstoreapp.domain.usecase.GetProductsUseCase
import com.androidstoreapp.domain.usecase.ToggleFavoriteUseCase
import com.androidstoreapp.domain.util.MainDispatcherRule
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductsViewModelTest {

    @get:Rule val mainRule = MainDispatcherRule()

    private val getProducts: GetProductsUseCase = mockk()
    private val getFavorites: GetFavoritesUseCase = mockk()
    private val toggle: ToggleFavoriteUseCase = mockk(relaxed = true)

    private fun buildVm() = ProductsViewModel(getProducts, getFavorites, toggle)

    @Test
    fun `initial uiState is Loading`() = runTest {
        coEvery { getProducts() } coAnswers { delay(500); Result.success(emptyList()) }
        every { getFavorites() } returns flowOf(emptySet())

        assertEquals(UiState.Loading, buildVm().uiState.value)
    }

    @Test
    fun `when getProducts succeeds, emits Content`() = runTest {
        val products = listOf(Product(1, "Shirt", 19.99, "url"))
        coEvery { getProducts() } returns Result.success(products)
        every { getFavorites() } returns flowOf(emptySet())

        val vm = buildVm()
        val state = vm.uiState.value
        assertTrue("Expected Content but got $state", state is UiState.Content)
        assertEquals(1, (state as UiState.Content).data.size)
    }

    @Test
    fun `product in favorites set has isFavorite true`() = runTest {
        val products = listOf(Product(1, "A", 10.0, ""), Product(2, "B", 20.0, ""))
        coEvery { getProducts() } returns Result.success(products)
        every { getFavorites() } returns flowOf(setOf(1))

        val vm = buildVm()
        val content = vm.uiState.value as UiState.Content
        assertTrue(content.data.first { it.id == 1 }.isFavorite)
        assertFalse(content.data.first { it.id == 2 }.isFavorite)
    }

    @Test
    fun `when getProducts fails, emits Error`() = runTest {
        coEvery { getProducts() } returns Result.failure(Exception("Network error"))
        every { getFavorites() } returns flowOf(emptySet())

        val vm = buildVm()
        val state = vm.uiState.value
        assertTrue("Expected Error but got $state", state is UiState.Error)
        assertEquals("Network error", (state as UiState.Error).message)
    }

    @Test
    fun `toggle delegates to use case with correct params`() = runTest {
        coEvery { getProducts() } returns Result.success(emptyList())
        every { getFavorites() } returns flowOf(emptySet())

        buildVm().apply {
            toggle(productId = 3, isFavorite = true)
            advanceUntilIdle()
        }
        coVerify { toggle(3, true) }
    }

    @Test
    fun `favorites update reactively when observeFavoriteIds emits new set`() = runTest {
        val favoritesFlow = MutableStateFlow<Set<Int>>(emptySet())
        coEvery { getProducts() } returns Result.success(listOf(Product(1, "A", 10.0, "")))
        every { getFavorites() } returns favoritesFlow

        val vm = buildVm()
        assertFalse((vm.uiState.value as UiState.Content).data.first().isFavorite)

        favoritesFlow.value = setOf(1)
        vm.uiState.test {
            assertTrue((awaitItem() as UiState.Content).data.first().isFavorite)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
