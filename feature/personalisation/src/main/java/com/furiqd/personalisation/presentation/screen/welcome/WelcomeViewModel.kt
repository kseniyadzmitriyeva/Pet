package com.furiqd.personalisation.presentation.screen.welcome

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel@Inject constructor() : ViewModel() {
    val _uiState = MutableStateFlow(WelcomeUiState())
    val uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<WelcomeEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun handleEvent(event: WelcomeEvent) {
        when (event) {
            WelcomeEvent.onGetStartedClicked -> {
                _uiState.value = _uiState.value.copy(isLoading = true)
                _uiEffect.trySend(WelcomeEffect.NavigateToNext)
            }
        }
    }
}