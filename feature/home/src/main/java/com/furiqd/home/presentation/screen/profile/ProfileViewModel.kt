package com.furiqd.home.presentation.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furiqd.domain.usecase.GetUserUseCase
import com.furiqd.home.domain.usecase.LogoutUseCase
import com.furiqd.home.domain.usecase.UpdateAvatarUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel@Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val updateAvatarUseCase: UpdateAvatarUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<ProfileEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getUserUseCase.invoke().collect { user ->
                _uiState.value = _uiState.value.copy(
                    user = UserProfile(
                        id = user?.id ?: "",
                        name = user?.name ?: "",
                        email = user?.email ?: "",
                        avatarUrl = user?.avatarId,
                        amountOfRooms = user?.roomIds?.size ?: 0
                    )
                )
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun handleEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.LogoutClicked -> {
                viewModelScope.launch {
                    logoutUseCase.invoke()
                    _uiEffect.send(ProfileEffect.NavigateToAuth) }
            }
            is ProfileEvent.BackClicked -> {
                viewModelScope.launch {
                    _uiEffect.send(ProfileEffect.NavigateToMain)
                }
            }
            is ProfileEvent.EditProfileClicked -> {
                viewModelScope.launch {
                    _uiEffect.send(ProfileEffect.NavigateToEditProfile)
                }
            }
            is ProfileEvent.AvatarClicked -> {
                viewModelScope.launch {
                    _uiEffect.send(ProfileEffect.AddAvatar)
                }
            }
            is ProfileEvent.AvatarSelected -> {
                viewModelScope.launch {
                    updateAvatarUseCase.invoke(event.bytes)
                }
            }
        }
    }
}