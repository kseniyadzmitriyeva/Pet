package com.furiqd.pairing.presentation.screen.pairingchoice

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class PairingChoiceViewModel@Inject constructor(
): ViewModel() {
    private val _uiState = MutableStateFlow(PairingChoiceState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<PairingChoiceEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: PairingChoiceEvent) {
        when (event) {
            is PairingChoiceEvent.OnCreateRoomClicked -> {
                _effect.trySend(PairingChoiceEffect.NavigateToCreateRoom)
            }

            is PairingChoiceEvent.OnJoinRoomClicked -> {
                _effect.trySend(PairingChoiceEffect.NavigateToJoinRoom)
            }

            is PairingChoiceEvent.OnDismiss -> {
                _effect.trySend(PairingChoiceEffect.Close)
            }
        }
    }
}