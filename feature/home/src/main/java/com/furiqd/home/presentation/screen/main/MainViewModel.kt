package com.furiqd.home.presentation.screen.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furiqd.shared.usecase.GetUserUseCase
import com.furiqd.home.domain.usecase.FetchRoomsUseCase
import com.furiqd.home.presentation.screen.main.mapper.toUiModel
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
    private val fetchRoomsUseCase: FetchRoomsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<MainEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        observeUserDataAndRooms()
    }

    private fun observeUserDataAndRooms() {
        viewModelScope.launch {
            getUserUseCase.invoke().collect { user ->
                val currentUserId = user?.id ?: ""

                _uiState.update { currentState ->
                    currentState.copy(
                        userId = currentUserId,
                        isLoading = false
                    )
                }
                if (currentUserId.isNotBlank()) {
                    fetchRoomsUseCase.invoke(currentUserId).collect { pairingRooms ->
                        _uiState.update { currentState ->
                            val mappedRooms = pairingRooms.map { room ->
                                room.toUiModel(isSelected = false)
                            }

                            val fullList = mappedRooms + RoomUiModel.Placeholder()
                            val updatedList = updateSelectedRoomInList(fullList, currentState.selectedRoom?.id)

                            currentState.copy(
                                rooms = updatedList,
                                isLoading = false
                            )
                        }
                        Log.d("MainViewModel", "Rooms fetched: $pairingRooms")
                    }
                }
            }
        }
    }
    private fun updateSelectedRoomInList(list: List<RoomUiModel>, selectedId: String?):
            List<RoomUiModel> {
        val idToSelect = selectedId ?: list.firstOrNull()?.id
        return list.map { room ->
            when (room) {
                is RoomUiModel.Active -> room.copy(isSelected = room.id == idToSelect)
                is RoomUiModel.Placeholder -> room.copy(isSelected = room.id == idToSelect)
                is RoomUiModel.WaitingForFriend -> room.copy(isSelected = room.id == idToSelect)
                is RoomUiModel.Expired -> room.copy(isSelected = room.id == idToSelect)
            }
        }
    }

    fun handleEvent(event: MainEvent) {
        when (event) {
            is MainEvent.CameraClicked -> {
                viewModelScope.launch {
                    _effect.send(MainEffect.TriggerPetAnimation)
                }
            }
            is MainEvent.ProfileClicked -> {
                viewModelScope.launch {
                    _effect.send(MainEffect.NavigateToProfile)
                }
            }
            is MainEvent.PetClicked -> {
                viewModelScope.launch {
                    _effect.send(MainEffect.TriggerPetAnimation)
                }
            }
            is MainEvent.CopyInviteCodeClicked -> {
                viewModelScope.launch {
                    _effect.send(MainEffect.CopyToClipboard(event.code))
                    //_effect.send(MainEffect.ShowToast("Код скопирован!"))
                }
            }
            is MainEvent.AddRoomClicked -> {
                viewModelScope.launch {
                    _effect.send(MainEffect.CreateRoom)
                }
            }
            is MainEvent.LeaveRoomClicked -> {
                viewModelScope.launch {
                    // TODO: Вызвать репозиторий / UseCase для удаления комнаты
                }
            }
            is MainEvent.InciteFriendClicked -> {
                viewModelScope.launch {
                    // TODO: Вызвать репозиторий / UseCase для пинга друга (Nudge)
                    _effect.send(MainEffect.ShowToast("Friend nudged!"))
                }
            }
            is MainEvent.RoomChangedToNextInPager -> {
                val targetRoomId = uiState.value.nextRoom?.id ?: return

                _uiState.update { currentState ->
                    val updatedRooms = currentState.rooms.map { room ->
                        when (room) {
                            is RoomUiModel.Active -> room.copy(isSelected = room.id == targetRoomId)
                            is RoomUiModel.Placeholder -> room.copy(isSelected = room.id == targetRoomId)
                            is RoomUiModel.WaitingForFriend -> room.copy(isSelected = room.id == targetRoomId)
                            is RoomUiModel.Expired -> room.copy(isSelected = room.id == targetRoomId)
                        }
                    }
                    currentState.copy(rooms = updatedRooms)
                }
            }
            is MainEvent.RoomChangedToPreviousInPager -> {
                val targetRoomId = uiState.value.previousRoom?.id ?: return

                _uiState.update { currentState ->
                    val updatedRooms = currentState.rooms.map { room ->
                        when (room) {
                            is RoomUiModel.Active -> room.copy(isSelected = room.id == targetRoomId)
                            is RoomUiModel.Placeholder -> room.copy(isSelected = room.id == targetRoomId)
                            is RoomUiModel.WaitingForFriend -> room.copy(isSelected = room.id == targetRoomId)
                            is RoomUiModel.Expired -> room.copy(isSelected = room.id == targetRoomId)
                        }
                    }
                    currentState.copy(rooms = updatedRooms)
                }
            }
            is MainEvent.OpenCreateRoomDialogClicked -> {
                viewModelScope.launch {
                    _effect.send(MainEffect.CreateRoom)
                }
            }
        }
    }
}