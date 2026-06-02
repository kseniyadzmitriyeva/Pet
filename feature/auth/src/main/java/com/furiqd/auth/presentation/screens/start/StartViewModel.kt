package com.furiqd.auth.presentation.screens.start

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class StartViewModel@Inject constructor() : ViewModel(){

    private val _uiEffect = Channel<StartEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun handleEvent(event: StartEvent) {
        when (event) {
            StartEvent.SignUpClicked -> {
                _uiEffect.trySend(StartEffect.NavigateToSignUp)
            }
            StartEvent.SignInClicked -> {
                _uiEffect.trySend(StartEffect.NavigateToSignIn)
            }
        }
    }
}