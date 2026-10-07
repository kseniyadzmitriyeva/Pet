package com.furiqd.home.presentation.screen.createroom

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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.furiqd.home.R
import com.furiqd.home.presentation.screen.createroom.CreateRoomState.Companion.petAvatars

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
                        val currentAvatarRes = CreateRoomState.petAvatars.getOrNull(state.petEgg - 1)
                            ?: com.furiqd.designsystem.R.drawable.green_egg

                        Image(
                            painter = painterResource(id = currentAvatarRes),
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

                // Поле ввода имени
                Text(
                    text = stringResource(R.string.create_room_name_label),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
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
                        .CreateRoomClicked(name, petAvatars[currentPetIndex])) },
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
    viewModel: CreateRoomViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CreateRoomEffect.NavigateToMain -> onNavigateToMain()
                is CreateRoomEffect.ShowToast -> {
                    // TODO: Show toast
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