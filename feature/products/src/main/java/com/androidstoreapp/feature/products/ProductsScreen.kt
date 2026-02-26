package com.androidstoreapp.feature.products

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.androidstoreapp.core.ui.UiState
import com.androidstoreapp.core.ui.components.ErrorView
import com.androidstoreapp.core.ui.components.LoadingView
import com.androidstoreapp.core.ui.components.ProductItem
import com.androidstoreapp.domain.model.Product
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProductsScreen(
    modifier: Modifier = Modifier,
    vm: ProductsViewModel = koinViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        when (val s = state) {
            is UiState.Loading -> LoadingView()
            is UiState.Error -> ErrorView(message = s.message, onRetry = vm::load)
            is UiState.Content -> ProductList(products = s.data, onToggle = vm::toggle)
        }
    }
}

@Composable
private fun ProductList(
    products: List<Product>,
    onToggle: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(products, key = { it.id }) { product ->
            ProductItem(product = product, onToggle = onToggle)
        }
    }
}
