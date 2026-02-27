package com.androidstoreapp.core.network.mapper

import com.androidstoreapp.core.network.NoInternetException
import com.androidstoreapp.domain.model.DomainException
import retrofit2.HttpException
import java.io.IOException

fun Throwable.toDomainException(): DomainException {
    return when (this) {
        is NoInternetException -> DomainException.NoNetwork()
        is HttpException -> DomainException.ServerError()
        is IOException -> DomainException.NoNetwork()
        else -> DomainException.Unknown(message)
    }
}

fun <T> Result<T>.mapErrorToDomain(): Result<T> {
    val exception = exceptionOrNull() ?: return this
    return Result.failure(exception.toDomainException())
}
