package com.androidstoreapp.core.network.repository

import com.androidstoreapp.core.network.ApiService
import com.androidstoreapp.core.network.NetworkMonitor
import com.androidstoreapp.core.network.mapper.mapErrorToDomain
import com.androidstoreapp.core.network.mapper.toDomain
import com.androidstoreapp.domain.model.User
import com.androidstoreapp.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryImpl(
    private val api: ApiService,
    private val networkMonitor: NetworkMonitor
) : UserRepository {

    override fun observeUser(): Flow<Result<User>> = 
        networkMonitor.isOnline
            .distinctUntilChanged()
            .flatMapLatest { isOnline ->
                flow {
                    if (isOnline) {
                        emit(getUser())
                    } else {
                        emit(getUser())
                    }
                }
            }

    override suspend fun getUser(): Result<User> =
        runCatching { api.getUser().toDomain() }.mapErrorToDomain()
}
