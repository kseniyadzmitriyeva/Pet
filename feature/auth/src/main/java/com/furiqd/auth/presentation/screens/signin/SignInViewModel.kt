package com.furiqd.auth.presentation.screens.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furiqd.auth.domain.usecase.AuthUseCases
import com.furiqd.auth.presentation.helper.AuthWithGoogleHelper
import com.furiqd.auth.presentation.helper.AuthWithGoogleHelper.AuthUiResult
import com.furiqd.auth.presentation.helper.InputValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val authHelper: AuthWithGoogleHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<SignInEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun handleEvent(event: SignInEvent, context: android.content.Context? = null) {
        when (event) {
            is SignInEvent.GoogleClicked -> {
                context?.let { signInWithGoogle(it) }
            }
            is SignInEvent.SignInClicked -> {
                _uiState.update { it.copy(isLoading = true) }
                signInWithEmail()
            }
            is SignInEvent.EmailChanged -> {
                val error = InputValidator.validateEmail(event.email)
                _uiState.update {
                    it.copy(
                        email = event.email,
                        emailError = error,
                        isSignInEnabled = it.password.isNotBlank() && error == null
                    )
                }
            }
            is SignInEvent.PasswordChanged -> {
                val error = InputValidator.validatePassword(event.password)
                _uiState.update { it.copy(
                    password = event.password,
                    passwordError = error,
                    isSignInEnabled = it.email.isNotBlank() && error == null) }
            }
            is SignInEvent.GoToSignUpClicked -> {
                viewModelScope.launch { _uiEffect.send(SignInEffect.NavigateToSignUp) }
            }
            is SignInEvent.BackClicked -> {
                viewModelScope.launch { _uiEffect.send(SignInEffect.NavigateBack) }
            }
            is SignInEvent.ForgotPasswordClicked -> {
                viewModelScope.launch { _uiEffect.send(SignInEffect.NavigateToForgotPassword) }
            }
        }
    }
    private fun signInWithGoogle(context: android.content.Context) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            when (val uiResult = authHelper.launchAuthUi(context)) {
                is AuthUiResult.GoogleSuccess -> {
                    val domainResult = authUseCases.signInWithGoogle(uiResult.idToken)

                    domainResult.onSuccess { isOnboardingCompleted ->
                        _uiState.update { it.copy(isLoading = false) }
                        _uiEffect.send(SignInEffect.NavigateToWelcome(isOnboardingCompleted))
                    }.onFailure { error ->
                        _uiState.update { it.copy(isLoading = false) }
                        _uiEffect.send(SignInEffect.ShowError(error.message ?: "Неизвестная ошибка"))
                    }
                }
                is AuthUiResult.Failure -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEffect.send(SignInEffect.ShowError(uiResult.error.message ?: "Неизвестная ошибка"))
                }
            }
        }
    }

    private fun signInWithEmail() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = authUseCases.signInWithEmailAndPassword(
                email = _uiState.value.email,
                password = _uiState.value.password
            )
            result.onSuccess { isOnboardingCompleted ->
                _uiState.update { it.copy(isLoading = false) }
                _uiEffect.send(SignInEffect.NavigateToWelcome(isOnboardingCompleted))
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false) }
                _uiEffect.send(SignInEffect.ShowError(error.message ?: "Неверный логин или пароль"))
            }
        }
    }
}
