package com.furiqd.personalisation.presentation.screen.onboarding

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.util.getColumnIndex
import coil.compose.AsyncImage
import com.furiqd.designsystem.config.AvatarConfig
import com.furiqd.personalisation.R
import com.furiqd.personalisation.presentation.element.AvatarSelectionDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onEvent: (OnboardingEvent) -> Unit,
    state: OnboardingState
) {
    var isDialogVisible by remember { mutableStateOf(false) }

    val isAvatarChosen = state.selectedAvatarId != null

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                val avatarScale by animateFloatAsState(
                    targetValue = if (isAvatarChosen) 1.05f else 1f,
                    label = "AvatarScale"
                )

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .scale(avatarScale)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(
                            width = 3.dp,
                            color = if (isAvatarChosen) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable { isDialogVisible = true },
                    contentAlignment = Alignment.Center
                ) {
                    val avatarModel = remember(state.localAvatarUri, state.selectedAvatarId) {
                        when {
                            !state.localAvatarUri.isNullOrBlank() -> {
                                state.localAvatarUri
                            }
                            state.selectedAvatarId.isNotBlank() -> {
                                val intId = state.selectedAvatarId.toIntOrNull()
                                if (intId != null && intId != 0) {
                                    AvatarConfig.getDrawableById(intId)
                                } else {
                                    state.selectedAvatarId
                                }
                            }
                            else -> {
                                com.furiqd.designsystem.R.drawable.avatar_girl_svgrepo_com__11_
                            }
                        }
                    }

                    AsyncImage(
                        model = avatarModel,
                        contentDescription = "Selected Avatar Preview",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = state.nickname,
                    onValueChange = { onEvent(OnboardingEvent.NicknameChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.onboarding_input_label)) },
                    placeholder = { Text(stringResource(R.string.onboarding_input_placeholder)) },
                    isError = state.isNicknameError,
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.secondary,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    supportingText = {
                        if (state.isNicknameError) {
                            Text(
                                text = stringResource(R.string.onboarding_error_nickname_length),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = state.name,
                    onValueChange = { onEvent(OnboardingEvent.NameChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(stringResource(R.string.onboarding_error_nickname_length)) },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { onEvent(OnboardingEvent.NextClicked) },
                    enabled = state.isNextButtonEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = stringResource(R.string.onboarding_btn_action),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (isDialogVisible) {
        AvatarSelectionDialog(
            currentSelectedId = state.selectedAvatarId,
            onAvatarSelected = { id ->
                onEvent(OnboardingEvent.CommonAvatarSelected(id))
            },
            onUploadCustomClick = {
                onEvent(OnboardingEvent.UploadCustomAvatarClicked)
            },
            onDismissRequest = {
                isDialogVisible = false
            }
        )
    }
}

@Composable
fun OnboardingRoute(
    onNavigateToNext: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val maxFileSize = 5 * 1024 * 1024 // 5 MB

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            val fileSize = context.contentResolver.query(
                selectedUri, null, null, null, null
            )?.use { cursor ->
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                cursor.moveToFirst()
                if (sizeIndex != -1) cursor.getLong(sizeIndex) else 0L
            } ?: 0L

            if (fileSize > maxFileSize) {
                return@let
            }

            val bytes = context.contentResolver.openInputStream(selectedUri)?.use { it.readBytes() }
            if (bytes != null) {
                viewModel.handleEvent(OnboardingEvent.CustomAvatarSelected(bytes, selectedUri.toString()))
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is OnboardingEffect.NavigateToNext -> onNavigateToNext()
                is OnboardingEffect.ShowError -> {}
                is OnboardingEffect.UploadAvatar -> {
                    imagePickerLauncher.launch("image/*")
                }
            }
        }
    }

    OnboardingScreen(
        state = state,
        onEvent = { event ->
            viewModel.handleEvent(event)
        }
    )
}