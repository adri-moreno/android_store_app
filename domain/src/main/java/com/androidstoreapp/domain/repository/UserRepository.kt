package com.androidstoreapp.domain.repository

import com.androidstoreapp.domain.model.User

interface UserRepository {
    suspend fun getUser(): Result<User>
}
