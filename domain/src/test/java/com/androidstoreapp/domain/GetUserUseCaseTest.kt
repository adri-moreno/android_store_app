package com.androidstoreapp.domain

import com.androidstoreapp.domain.model.User
import com.androidstoreapp.domain.repository.UserRepository
import com.androidstoreapp.domain.usecase.GetUserUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetUserUseCaseTest {

    private val repo: UserRepository = mockk()
    private val useCase = GetUserUseCase(repo)

    @Test
    fun `returns user when repo succeeds`() = runTest {
        val fakeUser = User(8, "John Doe", "john@email.com", "555-1234")
        coEvery { repo.getUser() } returns Result.success(fakeUser)

        assertEquals(fakeUser, useCase().getOrNull())
    }

    @Test
    fun `returns failure when repo fails`() = runTest {
        coEvery { repo.getUser() } returns Result.failure(Exception("404"))

        assertTrue(useCase().isFailure)
    }
}
