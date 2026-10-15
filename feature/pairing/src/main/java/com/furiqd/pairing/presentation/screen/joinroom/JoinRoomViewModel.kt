package com.furiqd.pairing.presentation.screen.joinroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furiqd.pairing.domain.usecase.JoinRoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinRoomViewModel @Inject constructor(
    private val joinRoomUseCase: JoinRoomUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(JoinRoomState.Content())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<JoinRoomEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: JoinRoomEvent) {
        when (event) {
            is JoinRoomEvent.CodeChanged -> {
                val filteredCode = event.code.take(_uiState.value.codeLength).uppercase()
                _uiState.update { it.copy(code = filteredCode) }
                if (filteredCode.length == _uiState.value.codeLength) {
                    submitCode(filteredCode)
                }
            }
            JoinRoomEvent.SubmitClicked -> {
                submitCode(_uiState.value.code)
            }
            JoinRoomEvent.BackClicked -> {
                viewModelScope.launch {
                    _effect.send(JoinRoomEffect.NavigateBack)
                }
            }
        }
    }

    private fun submitCode(code: String) {
        if (code.length < _uiState.value.codeLength || _uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            joinRoomUseCase(code).fold(
                onSuccess = { room ->
                    _uiState.update { it.copy(isLoading = false) }
                    _effect.send(JoinRoomEffect.NavigateToRoom(room))
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    _effect.send(
                        JoinRoomEffect.ShowToast(
                            error.localizedMessage ?: "Не удалось подключиться к комнате"
                        )
                    )
                }
            )
        }
    }
}