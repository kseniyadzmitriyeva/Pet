package com.furiqd.home.presentation.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furiqd.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val fetchRoomsUseCase: FetchRoomsUseCase,

) : ViewModel() {

    private val _uiState = MutableStateFlow(MainState())
    val uiState = _uiState.asStateFlow()
    private val _effect = Channel<MainEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        observeUserData()
    }

    private fun observeUserData() {
        viewModelScope.launch {
            getUserUseCase.invoke().collect { user ->
                if (user != null) {
                    _uiState.update { currentState ->
                        currentState.copy(

                        )
                    }
                } else {
                    _effect.send(MainEffect.NavigateToAuth)
                }
            }
        }
    }

    fun handleEvent(event: MainEvent) {
        when (event) {
            is MainEvent.PetClicked -> {
                viewModelScope.launch {
                    _effect.send(MainEffect.TriggerPetAnimation)
                }
            }
            is MainEvent.CopyInviteCodeClicked -> {
                viewModelScope.launch {
                    _effect.send(MainEffect.CopyToClipboard(event.code))
                    _effect.send(MainEffect.ShowToast("Код скопирован!"))
                }
            }
            is MainEvent.AddRoomClicked -> {
                viewModelScope.launch {
                    _effect.send(MainEffect.NavigateToCreateRoom)
                }
            }
            is MainEvent.RefreshRoom -> {
                viewModelScope.launch {
                    val currentSelectedId = uiState.value.selectedRoom?.id ?: return@launch

                    val newRoom = fetchRoomsUseCase.invoke(currentSelectedId)
                    if (newRoom != null) {
                        _uiState.update { currentState ->
                            val updatedRooms = currentState.rooms.map { room ->
                                if (room.id == currentSelectedId) newRoom else room
                            }
                            currentState.copy(rooms = updatedRooms)
                        }
                    }
                }
            }
            is MainEvent.LeaveRoomClicked -> {
                viewModelScope.launch {
                    _effect.send(MainEffect.LeaveRoom)
                }
            }
            is MainEvent.LogOutClicked -> {
                viewModelScope.launch {
                    _effect.send(MainEffect.NavigateToAuth)
                }
            }
            is MainEvent.InciteFriendClicked -> {

            }
        }
    }
}