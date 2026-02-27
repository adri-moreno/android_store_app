package com.androidstoreapp.feature.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidstoreapp.core.ui.UiState
import com.androidstoreapp.domain.model.Product
import com.androidstoreapp.domain.usecase.ObserveProductsUseCase
import com.androidstoreapp.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ProductsViewModel(
    private val observeProducts: ObserveProductsUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase
) : ViewModel() {

    private val refreshSignal = MutableStateFlow(Unit)

    val uiState: StateFlow<UiState<List<Product>>> = refreshSignal
        .flatMapLatest { observeProducts() }
        .map { result ->
            result.fold(
                onSuccess = { UiState.Content(it) },
                onFailure = { UiState.Error(it) }
            )
        }.stateIn(viewModelScope, SharingStarted.Eagerly, UiState.Loading)

    fun load() {
        refreshSignal.value = Unit
    }

    fun toggle(productId: Int, isFavorite: Boolean) {
        viewModelScope.launch { toggleFavorite(productId, isFavorite) }
    }
}
