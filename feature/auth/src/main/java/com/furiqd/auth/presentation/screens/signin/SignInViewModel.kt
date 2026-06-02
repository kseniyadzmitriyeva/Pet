package com.furiqd.auth.presentation.screens.signin

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
class SignInViewModel@Inject constructor() : ViewModel(){
    private val _uiState = MutableStateFlow(SignInState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<SignInEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun handleEvent(event: SignInEvent){
        when(event){
            is SignInEvent.SignInClicked ->{
                _uiState.update { it.copy(isLoading = true) }
                viewModelScope.launch {
                    _uiEffect.send(SignInEffect.NavigateToWelcome)
                }
            }
            is SignInEvent.GoToSignUpClicked -> {
                _uiState.update { it.copy(isLoading = true) }
                viewModelScope.launch { _uiEffect.send(SignInEffect.NavigateToSignUp) }
            }
            is SignInEvent.BackClicked -> {
                _uiState.update { it.copy(isLoading = true) }
                viewModelScope.launch { _uiEffect.send(SignInEffect.NavigateBack) }
            }
            is SignInEvent.GoogleClicked -> {
                _uiState.update { it.copy(isLoading = true) }
                _uiState.update { it.copy(isLoading = false) }
            }
            is SignInEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.email) }
            }
            is SignInEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.password) }
            }
            is SignInEvent.ForgotPasswordClicked -> {
                _uiState.update { it.copy(isLoading = true) }
                viewModelScope.launch { _uiEffect.send(SignInEffect.NavigateToForgotPassword) }
            }
        }

    }
}