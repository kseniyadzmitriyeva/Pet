package com.furiqd.home.presentation.screen.profile

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.furiqd.home.presentation.element.AvatarWithEditButton

@Composable
fun ProfileScreen(
    state: ProfileState,
    onEvent: (ProfileEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxSize()) {
        AvatarWithEditButton(
            avatarUrl = state.user?.avatarUrl,
            onAddAvatarClick = { onEvent(ProfileEvent.AvatarClicked) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = state.user?.name ?: "No name")
        Text(text = state.user?.email ?: "No email")
        Text(text = "Rooms: ${state.user?.amountOfRooms ?: 0}")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onEvent(ProfileEvent.EditProfileClicked) }) {
            Text(text = "Edit profile")
        }

    }
}

@Composable
fun ProfileRoute(
    onNavigateToMain: () -> Unit,
    onNavigateToAuth: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    //не смогла решить проблему с максимальным размером фоток
    val maxFileSize = 5 * 1024 * 1024

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            val fileSize = context.contentResolver.query(
                selectedUri, null, null, null, null)?.use { cursor ->
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                cursor.moveToFirst()
                cursor.getLong(sizeIndex)
            } ?: 0L

            if (fileSize > maxFileSize) {
                return@let
            }
            val bytes = context.contentResolver.openInputStream(selectedUri)?.use { it.readBytes() }
            if (bytes != null) {
                viewModel.handleEvent(ProfileEvent.AvatarSelected(bytes))
            }
        }
    }

    LaunchedEffect(Unit){
        viewModel.uiEffect.collect{ effect ->
            when(effect){
                is ProfileEffect.NavigateToMain -> onNavigateToMain()
                is ProfileEffect.NavigateToAuth -> onNavigateToAuth()
                is ProfileEffect.AddAvatar -> {
                    imagePickerLauncher.launch("image/*")
                }
                is ProfileEffect.NavigateToEditProfile -> onNavigateToEditProfile()
            }
        }
    }

    Scaffold() {
        paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                TopProfileBar(
                    onEvent = viewModel::handleEvent,
                    modifier = Modifier
                )
            }
            item {
                ProfileScreen(
                    state = state,
                    onEvent = viewModel::handleEvent,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun TopProfileBar(
    onEvent: (ProfileEvent) -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(16.dp)
    ) {
        IconButton(
            onClick = {onEvent(ProfileEvent.BackClicked)}
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }
        IconButton(
            onClick = {onEvent(ProfileEvent.LogoutClicked)}
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = "Logout"
            )
        }
    }
}