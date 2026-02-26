package com.androidstoreapp.domain

import com.androidstoreapp.domain.model.Product
import com.androidstoreapp.domain.repository.ProductRepository
import com.androidstoreapp.domain.usecase.GetProductsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetProductsUseCaseTest {

    private val repo: ProductRepository = mockk()
    private val useCase = GetProductsUseCase(repo)

    @Test
    fun `when repo returns list, use case returns same list`() = runTest {
        val fakeList = listOf(Product(1, "Shirt", 19.99, "url"))
        coEvery { repo.getProducts() } returns Result.success(fakeList)

        val result = useCase()

        assertTrue(result.isSuccess)
        assertEquals(fakeList, result.getOrNull())
    }

    @Test
    fun `when repo fails, use case propagates failure`() = runTest {
        coEvery { repo.getProducts() } returns Result.failure(Exception("Network"))

        assertTrue(useCase().isFailure)
    }
}
