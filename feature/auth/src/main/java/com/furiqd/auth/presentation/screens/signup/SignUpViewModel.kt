package com.furiqd.auth.presentation.screens.signup

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
class SignUpViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val authHelper: AuthWithGoogleHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<SignUpEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun handleEvent(event: SignUpEvent, context: android.content.Context? = null) {
        when (event) {
            is SignUpEvent.NameChanged -> {
                _uiState.update { it.copy(name = event.name) }
            }
            is SignUpEvent.EmailChanged -> {
                val error = InputValidator.validateEmail(event.email)
                _uiState.update { it.copy(
                    email = event.email,
                    emailError = error,
                    isSignUpEnabled = it.password.isNotBlank() && error == null) }
            }
            is SignUpEvent.PasswordChanged -> {
                val error = InputValidator.validatePassword(event.password)
                _uiState.update { it.copy(
                    password = event.password,
                    passwordError = error,
                    isSignUpEnabled = it.email.isNotBlank() && error == null) }
            }
            is SignUpEvent.PrivacyChecked -> {
                _uiState.update { it.copy(isPrivacyChecked = event.isChecked) }
            }
            SignUpEvent.GoogleClicked -> {
                context?.let { signInWithGoogle(it) }
            }
            SignUpEvent.SignUpClicked -> {
                _uiState.update { it.copy(isLoading = true) }
                signUpWithEmail()
            }
            SignUpEvent.BackClicked -> {
                viewModelScope.launch { _uiEffect.send(SignUpEffect.NavigateBack) }
            }
            SignUpEvent.GoToSignInClicked -> {
                viewModelScope.launch { _uiEffect.send(SignUpEffect.NavigateToSignIn) }
            }
        }
    }

    private fun signUpWithEmail() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = authUseCases.signUpWithEmailAndPassword(
                email = _uiState.value.email,
                password = _uiState.value.password
            )
            result.onSuccess { isOnboardingCompleted ->
                _uiState.update { it.copy(isLoading = false) }
                _uiEffect.send(SignUpEffect.NavigateToWelcome(isOnboardingCompleted))
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false) }
                _uiEffect.send(SignUpEffect.ShowError(error.message ?: "Неверный логин или пароль"))
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
                        _uiEffect.send(SignUpEffect.NavigateToWelcome(isOnboardingCompleted))
                    }.onFailure { error ->
                        _uiState.update { it.copy(isLoading = false) }
                        _uiEffect.send(SignUpEffect.ShowError(error.message ?: "Unknown error"))
                    }
                }
                is AuthUiResult.Failure -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEffect.send(SignUpEffect.ShowError(uiResult.error.message ?: "Unknown error"))
                }
            }
        }
    }
}
