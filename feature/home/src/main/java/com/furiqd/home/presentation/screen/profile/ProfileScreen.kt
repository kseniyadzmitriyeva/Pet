package com.furiqd.home.presentation.screen.profile

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.furiqd.home.R
import com.furiqd.home.presentation.element.AvatarWithEditButton
import com.furiqd.home.presentation.element.ProfileInfoCard
import com.furiqd.shared.ui.elements.BackButton
import com.furiqd.shared.ui.elements.ErrorSnackbar
import com.furiqd.shared.ui.elements.TextActionButton

private const val MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024 // 5 MB

@Composable
fun ProfileScreen(
    state: ProfileState,
    onEvent: (ProfileEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        AvatarWithEditButton(
            avatarUrl = state.user?.avatarUrl,
            onAddAvatarClick = { onEvent(ProfileEvent.AvatarClicked) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = state.user?.name ?: stringResource(R.string.profile_no_name),
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        ProfileInfoCard(
            email = state.user?.email ?: stringResource(R.string.profile_no_email),
            roomsCount = state.user?.amountOfRooms ?: 0
        )

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(24.dp))

        TextActionButton(
            text = stringResource(R.string.profile_action_edit),
            onClick = { onEvent(ProfileEvent.EditProfileClicked) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ProfileRoute(
    onNavigateToMain: () -> Unit,
    onNavigateToAuth: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
) {
    val viewModel: ProfileViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            val fileSize = context.contentResolver.query(
                selectedUri, null, null, null, null
            )?.use { cursor ->
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                cursor.moveToFirst()
                cursor.getLong(sizeIndex)
            } ?: 0L

            if (fileSize > MAX_FILE_SIZE_BYTES) {
                return@let
            }
            val bytes = context.contentResolver.openInputStream(selectedUri)?.use { it.readBytes() }
            if (bytes != null) {
                viewModel.handleEvent(ProfileEvent.AvatarSelected(bytes))
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is ProfileEffect.NavigateToMain -> onNavigateToMain()
                is ProfileEffect.NavigateToAuth -> onNavigateToAuth()
                is ProfileEffect.AddAvatar -> imagePickerLauncher.launch("image/*")
                is ProfileEffect.ShowToast -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message,
                        duration = SnackbarDuration.Short
                    )
                }
                is ProfileEffect.NavigateToEditProfile -> onNavigateToEditProfile()
            }
        }
    }

    Scaffold(
        topBar = {
            TopProfileBar(onEvent = viewModel::handleEvent)
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data -> ErrorSnackbar(snackbarData = data) }
            )
        }
    ) { paddingValues ->
        ProfileScreen(
            state = state,
            onEvent = viewModel::handleEvent,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopProfileBar(
    onEvent: (ProfileEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(R.string.profile_title),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        navigationIcon = {
            BackButton(
                onClick = { onEvent(ProfileEvent.BackClicked) }
            )
        },
        actions = {
            IconButton(
                onClick = { onEvent(ProfileEvent.LogoutClicked) }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = stringResource(R.string.profile_logout_description),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}