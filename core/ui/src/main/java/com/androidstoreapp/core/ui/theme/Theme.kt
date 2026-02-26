package com.androidstoreapp.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1E6FCC),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEBF3FC),
    secondary = Color(0xFF1A7F4B),
    surface = Color(0xFFFAFAFA),
    background = Color(0xFFF5F5F5),
    error = Color(0xFFC0392B)
)

@Composable
fun StoreTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography(),
        content = content
    )
}
