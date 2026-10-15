package com.furiqd.pairing.presentation.screen.pairingchoice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.furiqd.pairing.R
import com.furiqd.shared.ui.elements.ErrorSnackbar
@Composable
private fun ChoiceCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    OutlinedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun PairingChoiceRoute(    onNavigateToCreate: () -> Unit,
                           onNavigateToJoin: () -> Unit,
                           onDismiss: () -> Unit,
                           viewModel: PairingChoiceViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PairingChoiceEffect.NavigateToCreateRoom -> onNavigateToCreate()
                is PairingChoiceEffect.NavigateToJoinRoom -> onNavigateToJoin()
                is PairingChoiceEffect.Close -> onDismiss()
                is PairingChoiceEffect.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }
PairingChoiceDialog(
        state = state,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::handleEvent
    )
}

@Composable
fun PairingChoiceDialog(
    state: PairingChoiceState,
    snackbarHostState: SnackbarHostState,
    onEvent: (PairingChoiceEvent) -> Unit
) {
    Dialog(onDismissRequest = { onEvent(PairingChoiceEvent.OnDismiss) }) {
        Box(contentAlignment = Alignment.BottomCenter) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.pairing_choice_title),
                            style = MaterialTheme.typography.headlineSmall
                        )
                        IconButton(onClick = { onEvent(PairingChoiceEvent.OnDismiss) }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.pairing_choice_close_content_description)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    ChoiceCard(
                        title = stringResource(R.string.pairing_choice_create_room_title),
                        description = stringResource(R.string.pairing_choice_create_room_description),
                        icon = Icons.Default.Add,
                        onClick = { onEvent(PairingChoiceEvent.OnCreateRoomClicked) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ChoiceCard(
                        title = stringResource(R.string.pairing_choice_join_room_title),
                        description = stringResource(R.string.pairing_choice_join_room_description),
                        icon = Icons.Default.GroupAdd,
                        onClick = { onEvent(PairingChoiceEvent.OnJoinRoomClicked) }
                    )
                }
            }
            SnackbarHost(hostState = snackbarHostState) { data ->
                ErrorSnackbar(data)
            }
        }
    }
}