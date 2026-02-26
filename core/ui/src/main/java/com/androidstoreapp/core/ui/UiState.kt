package com.androidstoreapp.core.ui

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Error(val message: String) : UiState<Nothing>()
    data class Content<T>(val data: T) : UiState<T>()
}
