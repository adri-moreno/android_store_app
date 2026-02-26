package com.androidstoreapp.domain.model

data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val image: String,
    val description: String = "",
    val category: String = "",
    val rating: Double = 0.0,
    val isFavorite: Boolean = false
)
