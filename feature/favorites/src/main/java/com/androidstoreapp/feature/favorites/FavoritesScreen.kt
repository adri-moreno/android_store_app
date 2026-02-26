package com.androidstoreapp.feature.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.androidstoreapp.core.ui.R
import com.androidstoreapp.core.ui.UiState
import com.androidstoreapp.core.ui.components.ErrorView
import com.androidstoreapp.core.ui.components.LoadingView
import com.androidstoreapp.core.ui.components.ProductItem
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    vm: FavoritesViewModel = koinViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()

    Column(modifier.fillMaxSize().testTag("FavoritesScreen")) {
        Text(
            text = stringResource(R.string.title_my_favorites),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        when (val s = state) {
            is UiState.Loading -> LoadingView()
            is UiState.Error -> ErrorView(message = s.message, onRetry = {})
            is UiState.Content -> {
                if (s.data.isEmpty()) {
                    EmptyFavoritesView()
                } else {
                    LazyColumn(contentPadding = PaddingValues(vertical = 8.dp)) {
                        items(s.data, key = { it.id }) { product ->
                            ProductItem(product = product, onToggle = vm::toggle)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyFavoritesView() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stringResource(R.string.empty_favorites),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
