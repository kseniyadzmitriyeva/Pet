package com.furiqd.auth.presentation.screens.signup

import android.util.Log
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
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<SignUpEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun handleEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.EmailChanged -> {
                val error = InputValidator.validateEmail(event.email)
                if (error != null) {
                    Log.d("Sign Up","Email is not valid $error")
                }
                _uiState.update { state ->
                    val newState = state.copy(email = event.email, emailError = error)
                    newState.copy(isSignUpEnabled = newState.canSignUp())
                }
            }
            is SignUpEvent.PasswordChanged -> {
                val error = InputValidator.validatePassword(event.password)
                if (error != null) {
                    Log.d("Sign Up","Password is not valid $error")
                }
                _uiState.update { state ->
                    val newState = state.copy(password = event.password, passwordError = error)
                    newState.copy(isSignUpEnabled = newState.canSignUp())
                }
            }
            is SignUpEvent.PrivacyChecked -> {
                _uiState.update { state ->
                    Log.d("Sign Up","Privacy checked")
                    val newState = state.copy(isPrivacyChecked = event.isChecked)
                    newState.copy(isSignUpEnabled = newState.canSignUp())
                }
            }
            SignUpEvent.SignUpClicked -> {
                _uiState.update { it.copy(isLoading = true) }
                signUpWithEmail()
            }
            SignUpEvent.GoogleClicked -> {
                viewModelScope.launch { _uiEffect.send(SignUpEffect.LaunchGoogleAuth) }
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
                Log.d("Sign Up","Sign up success")
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false) }
                _uiEffect.send(SignUpEffect.ShowError(error.message ?: "Неверный логин или пароль"))
            }
        }
    }

    fun onGoogleTokenReceived(idToken: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            authUseCases.signInWithGoogle(idToken)
                .onSuccess { isOnboardingCompleted ->
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEffect.send(SignUpEffect.NavigateToWelcome(isOnboardingCompleted))
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEffect.send(SignUpEffect.ShowError(error.message ?: "Unknown error"))
                }
        }
    }

    fun onGoogleAuthFailed(errorMessage: String) {
        _uiState.update { it.copy(isLoading = false) }
        viewModelScope.launch {
            _uiEffect.send(SignUpEffect.ShowError(errorMessage))
        }
    }

    private fun SignUpState.canSignUp(): Boolean {
        return email.isNotBlank() &&
                emailError == null &&
                password.isNotBlank() &&
                passwordError == null &&
                isPrivacyChecked
    }
}
