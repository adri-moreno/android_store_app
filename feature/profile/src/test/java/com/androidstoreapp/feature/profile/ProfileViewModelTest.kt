package com.androidstoreapp.feature.profile

import com.androidstoreapp.core.ui.UiState
import com.androidstoreapp.domain.model.User
import com.androidstoreapp.domain.usecase.GetFavoritesUseCase
import com.androidstoreapp.domain.usecase.GetUserUseCase
import com.androidstoreapp.domain.util.MainDispatcherRule
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    @get:Rule val mainRule = MainDispatcherRule()

    private val getUser: GetUserUseCase = mockk()
    private val getFavorites: GetFavoritesUseCase = mockk()

    @Test
    fun `favoriteCount equals number of ids in favorites set`() = runTest {
        coEvery { getUser() } returns Result.success(User(8, "John", "j@m.com", "555"))
        every { getFavorites() } returns flowOf(setOf(1, 2, 3))

        val vm = ProfileViewModel(getUser, getFavorites)
        assertEquals(3, vm.uiState.value.favoriteCount)
    }

    @Test
    fun `when getUser succeeds, userState is Content`() = runTest {
        val user = User(8, "John", "j@m.com", "555")
        coEvery { getUser() } returns Result.success(user)
        every { getFavorites() } returns flowOf(emptySet())

        val vm = ProfileViewModel(getUser, getFavorites)
        assertTrue(vm.uiState.value.userState is UiState.Content)
    }

    @Test
    fun `when getUser fails, userState is Error`() = runTest {
        coEvery { getUser() } returns Result.failure(Exception("404"))
        every { getFavorites() } returns flowOf(emptySet())

        val vm = ProfileViewModel(getUser, getFavorites)
        assertTrue(vm.uiState.value.userState is UiState.Error)
    }

    @Test
    fun `favoriteCount updates reactively`() = runTest {
        val favFlow = MutableStateFlow<Set<Int>>(emptySet())
        coEvery { getUser() } returns Result.success(User(8, "John", "j@m.com", "555"))
        every { getFavorites() } returns favFlow

        val vm = ProfileViewModel(getUser, getFavorites)
        assertEquals(0, vm.uiState.value.favoriteCount)

        favFlow.value = setOf(1, 2)
        vm.uiState.test {
            assertEquals(2, awaitItem().favoriteCount)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
