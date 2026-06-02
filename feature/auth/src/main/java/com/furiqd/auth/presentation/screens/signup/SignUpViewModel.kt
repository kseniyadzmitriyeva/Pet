package com.furiqd.auth.presentation.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel@Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<SignUpEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun handleEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.NameChanged -> {
                _uiState.update { it.copy(name = event.name) }
            }
            is SignUpEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.email) }
            }
            is SignUpEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.password) }
            }
            is SignUpEvent.PrivacyChecked -> {
                _uiState.update { it.copy(isPrivacyChecked = event.isChecked) }
            }
            SignUpEvent.GoogleClicked -> { /* Логика Google */ }
            SignUpEvent.SignUpClicked -> {
                _uiState.update { it.copy(isLoading = true) }
                viewModelScope.launch {
                    _uiEffect.send(SignUpEffect.NavigateToWelcome)
                }
            }
            SignUpEvent.BackClicked -> {
                viewModelScope.launch { _uiEffect.send(SignUpEffect.NavigateBack) }
            }
            SignUpEvent.GoToSignInClicked -> {
                viewModelScope.launch { _uiEffect.send(SignUpEffect.NavigateToSignIn) }
            }
        }
    }
}