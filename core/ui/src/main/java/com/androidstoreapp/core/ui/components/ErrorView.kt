package com.androidstoreapp.core.ui.components

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.androidstoreapp.core.ui.R
import com.androidstoreapp.domain.model.DomainException

@Composable
fun ErrorView(
    throwable: Throwable?,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val message = when (throwable) {
        is DomainException.NoNetwork -> stringResource(R.string.error_no_internet)
        is DomainException.ServerError -> stringResource(R.string.error_server)
        is DomainException.Unknown -> throwable.originalMessage ?: stringResource(R.string.error_unknown)
        else -> throwable?.message ?: stringResource(R.string.error_unknown)
    }

    val isNoNetwork = throwable is DomainException.NoNetwork
    val buttonText = if (isNoNetwork) R.string.action_settings else R.string.action_retry
    val buttonAction = {
        if (isNoNetwork) context.startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
        else onRetry()
    }

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Icon(
            Icons.Default.Warning,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Button(onClick = buttonAction) {
            Text(stringResource(buttonText))
        }
    }
}
