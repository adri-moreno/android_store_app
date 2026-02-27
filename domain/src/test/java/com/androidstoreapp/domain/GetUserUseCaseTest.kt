package com.androidstoreapp.domain

import com.androidstoreapp.domain.model.User
import com.androidstoreapp.domain.repository.UserRepository
import com.androidstoreapp.domain.usecase.GetUserUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
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
        every { repo.observeUser() } returns flowOf(Result.success(fakeUser))

        assertEquals(fakeUser, useCase().first().getOrNull())
    }

    @Test
    fun `returns failure when repo fails`() = runTest {
        every { repo.observeUser() } returns flowOf(Result.failure(Exception("404")))

        assertTrue(useCase().first().isFailure)
    }
}
