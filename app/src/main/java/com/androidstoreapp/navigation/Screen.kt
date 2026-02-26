package com.androidstoreapp.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.ui.graphics.vector.ImageVector
import com.androidstoreapp.core.ui.R

sealed class Screen(val route: String, @param:StringRes val titleResId: Int, val icon: ImageVector) {
    object Products : Screen("products", R.string.title_products, Icons.Default.ShoppingBag)
    object Favorites : Screen("favorites", R.string.title_favorites, Icons.Default.Favorite)
    object Profile : Screen("profile", R.string.title_profile, Icons.Default.Person)
}

val bottomNavItems = listOf(Screen.Products, Screen.Favorites, Screen.Profile)
