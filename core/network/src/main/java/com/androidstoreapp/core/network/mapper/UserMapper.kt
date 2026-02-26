package com.androidstoreapp.core.network.mapper

import com.androidstoreapp.core.network.dto.UserDto
import com.androidstoreapp.domain.model.User

fun UserDto.toDomain() = User(
    id = id,
    fullName = "${name.firstname} ${name.lastname}",
    email = email,
    phone = phone,
    street = address.street,
    city = address.city
)
