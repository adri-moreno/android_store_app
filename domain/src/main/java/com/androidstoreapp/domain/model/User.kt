package com.androidstoreapp.domain.model

data class User(
    val id: Int,
    val fullName: String,
    val email: String,
    val phone: String,
    val street: String = "",
    val city: String = ""
)
