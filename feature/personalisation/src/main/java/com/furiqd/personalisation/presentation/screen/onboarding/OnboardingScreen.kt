package com.furiqd.personalisation.presentation.screen.onboarding

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.furiqd.personalisation.presentation.element.AvatarSelectionDialog
import com.furiqd.personalisation.R
import com.furiqd.personalisation.presentation.element.AvatarPickerSection
import com.furiqd.shared.ui.elements.LoadingScreen
import com.furiqd.shared.ui.elements.SharedTextField
import com.furiqd.shared.ui.elements.TextActionButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onEvent: (OnboardingEvent) -> Unit,
    state: OnboardingState
) {
    var isDialogVisible by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        if (state.isLoading) {
            LoadingScreen()
        } else {
            OnboardingMainContent(
                state = state,
                innerPadding = innerPadding,
                onEvent = onEvent,
                onOpenDialog = { isDialogVisible = true }
            )
        }

        if (isDialogVisible) {
            AvatarSelectionDialog(
                onUploadCustomClick = {
                    onEvent(OnboardingEvent.UploadCustomAvatarClicked)
                },
                onDismissRequest = {
                    isDialogVisible = false
                }
            )
        }
    }
}

@Composable
private fun OnboardingMainContent(
    state: OnboardingState,
    innerPadding: PaddingValues,
    onEvent: (OnboardingEvent) -> Unit,
    onOpenDialog: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        AvatarPickerSection(
            localAvatarUri = state.localAvatarUri,
            isAvatarChosen = state.selectedCustomBytes != null,
            onClick = onOpenDialog
        )

        Spacer(modifier = Modifier.height(32.dp))

        SharedTextField(
            value = state.nickname,
            onValueChange = { onEvent(OnboardingEvent.NicknameChanged(it)) },
            label = stringResource(R.string.onboarding_input_label),
            placeholder = stringResource(R.string.onboarding_input_placeholder),
            isError = state.isNicknameError,
            errorMessage = stringResource(R.string.onboarding_error_nickname_length)
        )

        Spacer(modifier = Modifier.height(24.dp))

        SharedTextField(
            value = state.name,
            onValueChange = { onEvent(OnboardingEvent.NameChanged(it)) },
            placeholder = stringResource(R.string.onboarding_input_placeholder),
            label = stringResource(R.string.onboarding_input_label),
            isError = state.isNameError,
            shape = RoundedCornerShape(16.dp) ,
            errorMessage = stringResource(R.string.onboarding_error_nickname_length)
        )

        Spacer(modifier = Modifier.weight(1f))

        TextActionButton(
            text = stringResource(R.string.onboarding_btn_action),
            enabled = state.isNextButtonEnabled,
            onClick = { onEvent(OnboardingEvent.NextClicked) }
        )

        Spacer(modifier = Modifier.height(16.dp))
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