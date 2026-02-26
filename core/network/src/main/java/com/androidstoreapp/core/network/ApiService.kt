package com.androidstoreapp.core.network

import com.androidstoreapp.core.network.dto.ProductDto
import com.androidstoreapp.core.network.dto.UserDto
import retrofit2.http.GET

interface ApiService {

    @GET("products")
    suspend fun getProducts(): List<ProductDto>

    @GET("users/8")
    suspend fun getUser(): UserDto
}
