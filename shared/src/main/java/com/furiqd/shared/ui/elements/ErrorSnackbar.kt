package com.furiqd.shared.ui.elements

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ErrorSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier
) {
    val actionLabel = snackbarData.visuals.actionLabel

    Snackbar(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        containerColor = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
        actionContentColor = MaterialTheme.colorScheme.onErrorContainer, // Правильный контраст для кнопки на errorContainer
        dismissActionContentColor = MaterialTheme.colorScheme.onErrorContainer,
        action = if (actionLabel != null) {
            {
                TextButton(
                    onClick = { snackbarData.performAction() }
                ) {
                    Text(
                        text = actionLabel,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else null,
        dismissAction = if (snackbarData.visuals.withDismissAction) {
            {
                IconButton(
                    onClick = { snackbarData.dismiss() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
                }
            }
        } else null
    ) {
        Text(
            text = snackbarData.visuals.message,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}