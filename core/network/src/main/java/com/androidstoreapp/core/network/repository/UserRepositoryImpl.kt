package com.androidstoreapp.core.network.repository

import com.androidstoreapp.core.network.ApiService
import com.androidstoreapp.core.network.mapper.toDomain
import com.androidstoreapp.domain.model.User
import com.androidstoreapp.domain.repository.UserRepository

class UserRepositoryImpl(private val api: ApiService) : UserRepository {

    override suspend fun getUser(): Result<User> =
        runCatching { api.getUser().toDomain() }
}
