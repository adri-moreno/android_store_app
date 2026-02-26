package com.androidstoreapp.feature.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidstoreapp.core.ui.UiState
import com.androidstoreapp.domain.model.Product
import com.androidstoreapp.domain.usecase.GetFavoritesUseCase
import com.androidstoreapp.domain.usecase.GetProductsUseCase
import com.androidstoreapp.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val getProducts: GetProductsUseCase,
    private val getFavorites: GetFavoritesUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase
) : ViewModel() {

    private val _result = MutableStateFlow<Result<List<Product>>?>(null)

    val uiState: StateFlow<UiState<List<Product>>> =
        combine(_result.filterNotNull(), getFavorites()) { result, favIds ->
            result.fold(
                onSuccess = { list ->
                    UiState.Content(list.map { it.copy(isFavorite = it.id in favIds) })
                },
                onFailure = { UiState.Error(it.message ?: "Error desconocido") }
            )
        }.stateIn(viewModelScope, SharingStarted.Eagerly, UiState.Loading)

    init { load() }

    fun load() { viewModelScope.launch { _result.value = getProducts() } }

    fun toggle(productId: Int, isFavorite: Boolean) {
        viewModelScope.launch { toggleFavorite(productId, isFavorite) }
    }
}
