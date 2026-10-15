package com.furiqd.pairing.presentation.screen.createroom

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.furiqd.shared.config.PetEggConfig
import com.furiqd.pairing.R

@Composable
fun CreateRoomDialog(
    onEvent: (CreateRoomEvent) -> Unit,
    state: CreateRoomState
) {

    var name by remember { mutableStateOf("") }
    var currentPetIndex by remember { mutableIntStateOf(0) }

    Dialog(
        onDismissRequest = { onEvent(CreateRoomEvent.DismissDialog) },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.create_room_title),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = {
                        onEvent(CreateRoomEvent.PreviousEggClicked)
                    }) {
                        Icon(Icons.Default.ChevronLeft, contentDescription = null, modifier = Modifier.size(32.dp))
                    }

                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        val eggDrawableRes = PetEggConfig.getDrawableByType(state.petEggType)

                        Image(
                            painter = painterResource(id = eggDrawableRes),
                            contentDescription = stringResource(R.string.create_room_pet_description),
                            modifier = Modifier.size(80.dp)
                        )
                    }

                    IconButton(onClick = {
                        onEvent(CreateRoomEvent.NextEggClicked)
                    }) {
                        Icon(Icons.Default.ChevronRight, contentDescription = null, modifier = Modifier.size(32.dp))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.create_room_name_label),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        onEvent(CreateRoomEvent.NameChanged(name))
                        Log.d("CreateRoom", "Name changed to $name, state: $state")},
                    placeholder = { Text(stringResource(R.string.create_room_name_hint)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { onEvent(CreateRoomEvent
                        .CreateRoomClicked(state.petName, state.petEggType)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = name.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = stringResource(R.string.create_room_action_create),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold)
                    )
                }

                TextButton(
                    onClick = { onEvent(CreateRoomEvent.DismissDialog) },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.create_room_action_cancel),
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

@Composable
fun CreateRoomRoute(
    onNavigateToMain: () -> Unit,
    onNavigateToHost: (roomId: String) -> Unit,
    viewModel: CreateRoomViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CreateRoomEffect.NavigateToMain -> onNavigateToMain()
                is CreateRoomEffect.NavigateToHost -> onNavigateToHost(effect.roomId)
                is CreateRoomEffect.ShowToast -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    CreateRoomDialog(
        onEvent = viewModel::handleEvent,
        state = state
    )

}

@Preview(showBackground = true)
@Composable
fun CreateRoomDialogPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            CreateRoomDialog(
                onEvent = {},
                state = CreateRoomState()
            )
        }
    }
}