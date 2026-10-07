package com.furiqd.home.presentation.screen.createroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furiqd.home.domain.usecase.CreateRoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateRoomViewModel@Inject constructor(
    private val createRoomUseCase: CreateRoomUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateRoomState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<CreateRoomEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: CreateRoomEvent){
        when(event){
            is CreateRoomEvent.CreateRoomClicked -> {
                viewModelScope.launch {
                    _uiState.value.copy(isLoading = true)
                    val result = createRoomUseCase.invoke(
                        uiState.value.petName,
                        uiState.value.petEgg)
                    result.onSuccess {
                        _effect.trySend(CreateRoomEffect.NavigateToMain)
                    }
                    result.onFailure {
                        _effect.trySend(CreateRoomEffect
                            .ShowToast(it.message ?: "Unknown error"))
                    }
                }

            }
            is CreateRoomEvent.DismissDialog -> {
                _effect.trySend(CreateRoomEffect.NavigateToMain)
            }
            is CreateRoomEvent.NameChanged -> {
                _uiState.value.copy(
                    petName = event.name
                )
            }
            is CreateRoomEvent.NextEggClicked -> {
                val totalAvatars = CreateRoomState.petAvatars.size

                _uiState.value = uiState.value.copy(
                    petEgg = (uiState.value.petEgg + 1) % totalAvatars
                )
            }
            is CreateRoomEvent.PreviousEggClicked -> {
                val totalAvatars = CreateRoomState.petAvatars.size

                _uiState.value = uiState.value.copy(
                    petEgg = (uiState.value.petEgg - 1 + totalAvatars) % totalAvatars
                )
            }
        }
    }
}