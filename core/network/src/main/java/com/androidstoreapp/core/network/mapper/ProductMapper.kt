package com.androidstoreapp.core.network.mapper

import com.androidstoreapp.core.network.dto.ProductDto
import com.androidstoreapp.domain.model.Product

fun ProductDto.toDomain() = Product(
    id = id,
    title = title,
    price = price,
    image = image,
    description = description,
    category = category,
    rating = rating.rate
)
