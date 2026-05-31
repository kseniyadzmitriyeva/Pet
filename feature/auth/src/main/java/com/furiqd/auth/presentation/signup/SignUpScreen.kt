package com.furiqd.auth.presentation.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.furiqd.auth.R
import com.furiqd.designsystem.R.drawable
import com.furiqd.designsystem.theme.ItsAliveTheme
import dagger.hilt.android.AndroidEntryPoint

@Composable
fun SignUpScreen(
    state: SignUpUiState,
    onEvent: (SignUpUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        IconButton(
            onClick = { onEvent(SignUpUiEvent.BackClicked) },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = drawable.arrow_narrow_left_icon_151607),
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(id = R.string.auth_signup_title),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onEvent(SignUpUiEvent.GoogleClicked) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.google_logo),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = stringResource(id = R.string.auth_continue_google), fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(id = R.string.auth_or_email),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = state.name,
            onValueChange = { onEvent(SignUpUiEvent.NameChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(id = R.string.auth_placeholder_name)) },
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = { onEvent(SignUpUiEvent.EmailChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(id = R.string.auth_placeholder_email)) },
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = { onEvent(SignUpUiEvent.PasswordChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(id = R.string.auth_placeholder_password)) },
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.auth_privacy_text),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                fontSize = 14.sp
            )
            Checkbox(
                checked = state.isPrivacyChecked,
                onCheckedChange = { onEvent(SignUpUiEvent.PrivacyChecked(it)) },            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onEvent(SignUpUiEvent.SignUpClicked) },
            enabled = state.isSignUpEnabled && !state.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
            } else {
                Text(text = stringResource(id = R.string.auth_btn_get_started), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SignUpRoute(
    onNavigateToWelcome: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                SignUpUiEffect.NavigateToWelcome -> onNavigateToWelcome()
                SignUpUiEffect.NavigateBack -> onNavigateBack()
                is SignUpUiEffect.ShowError -> { /* Показать Snackbar */ }
            }
        }
    }

    SignUpScreen(
        state = state,
        onEvent = viewModel::handleEvent
    )
}

@Preview(name = "Dark Theme", showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun PreviewStartScreenDark() {
    ItsAliveTheme(darkTheme = true) {
        val state = SignUpUiState()
        val viewModel = SignUpViewModel()
        SignUpScreen(
            state = state,
            onEvent = viewModel::handleEvent
        )
    }
}

@Preview(name = "Light Theme", showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun PreviewStartScreenLight() {
    ItsAliveTheme(darkTheme = false) {
        val state = SignUpUiState()
        val viewModel = SignUpViewModel()
        SignUpScreen(
            state = state,
            onEvent = viewModel::handleEvent
        )
    }
}