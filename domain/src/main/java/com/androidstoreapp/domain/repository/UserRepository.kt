package com.androidstoreapp.domain.repository

import com.androidstoreapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeUser(): Flow<Result<User>>
    suspend fun getUser(): Result<User>
}
