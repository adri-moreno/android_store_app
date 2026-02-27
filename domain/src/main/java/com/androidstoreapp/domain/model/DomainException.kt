package com.androidstoreapp.domain.model

sealed class DomainException : Exception() {
    class NoNetwork : DomainException()
    class ServerError : DomainException()
    data class Unknown(val originalMessage: String? = null) : DomainException()
}
