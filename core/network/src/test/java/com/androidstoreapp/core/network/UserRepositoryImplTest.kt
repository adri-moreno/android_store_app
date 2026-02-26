package com.androidstoreapp.core.network

import com.androidstoreapp.core.network.dto.AddressDto
import com.androidstoreapp.core.network.dto.NameDto
import com.androidstoreapp.core.network.dto.UserDto
import com.androidstoreapp.core.network.repository.UserRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.IOException

class UserRepositoryImplTest {

    private val api: ApiService = mockk()
    private val repo = UserRepositoryImpl(api)

    @Test
    fun `getUser maps DTO to domain User`() = runTest {
        val dto = UserDto(
            id = 8, email = "john@mail.com", username = "jdoe",
            name = NameDto("John", "Doe"), phone = "555-1234",
            address = AddressDto("Main St", "Springfield")
        )
        coEvery { api.getUser() } returns dto

        val result = repo.getUser()

        assertTrue(result.isSuccess)
        with(result.getOrNull()!!) {
            assertEquals(8, id)
            assertEquals("John Doe", fullName)
            assertEquals("john@mail.com", email)
            assertEquals("555-1234", phone)
        }
    }

    @Test
    fun `getUser returns failure when api throws`() = runTest {
        coEvery { api.getUser() } throws IOException("404")

        assertTrue(repo.getUser().isFailure)
    }
}
