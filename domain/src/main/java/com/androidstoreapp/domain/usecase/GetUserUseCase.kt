package com.androidstoreapp.domain.usecase

import com.androidstoreapp.domain.model.User
import com.androidstoreapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUserUseCase(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<Result<User>> = repository.observeUser()
}
