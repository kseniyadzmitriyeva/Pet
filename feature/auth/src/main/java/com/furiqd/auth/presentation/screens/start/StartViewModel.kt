package com.furiqd.auth.presentation.screens.start

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StartViewModel@Inject constructor() : ViewModel(){
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