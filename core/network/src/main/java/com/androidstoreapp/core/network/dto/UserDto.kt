package com.androidstoreapp.core.network.dto

data class UserDto(
    val id: Int,
    val email: String,
    val username: String,
    val name: NameDto,
    val phone: String,
    val address: AddressDto
)

data class NameDto(val firstname: String, val lastname: String)

data class AddressDto(val street: String = "", val city: String = "")
