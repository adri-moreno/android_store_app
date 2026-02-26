package com.androidstoreapp.domain.usecase

import com.androidstoreapp.domain.model.User
import com.androidstoreapp.domain.repository.UserRepository

class GetUserUseCase(private val repo: UserRepository) {
    suspend operator fun invoke(): Result<User> = repo.getUser()
}
