package com.furiqd.auth.presentation.start

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class StartViewModel {
    fun handleEvent(event: StartEvent) {
        when (event) {
            StartEvent.SignUpClicked -> {
                // Navigate to sign up screen
            }
            StartEvent.SignInClicked -> {
                // Navigate to sign in screen
            }
        }
    }
}