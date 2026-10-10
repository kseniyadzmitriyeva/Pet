package com.furiqd.personalisation.presentation.screen.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furiqd.domain.model.User
import com.furiqd.domain.repository.UserRepository
import com.furiqd.personalisation.domain.GetAvatarsUseCase
import com.furiqd.personalisation.domain.SaveUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val getAvailableAvatarsUseCase: GetAvatarsUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingState())
    val uiState = _uiState.asStateFlow()
    private val _effect = Channel<OnboardingEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        observeAvatars()
    }

    fun handleEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.NicknameChanged -> {
                _uiState.update { currentState ->
                    val isLengthError = event.nickname.length !in 2..15
                    currentState.copy(
                        nickname = event.nickname,
                        isNicknameError = event.nickname.isNotEmpty() && isLengthError,
                        isNextButtonEnabled = !isLengthError && currentState.selectedAvatarId != null
                    )
                }
            }
            is OnboardingEvent.NameChanged -> {
                _uiState.update { currentState ->
                    val isLengthError = event.name.length !in 2..15
                    currentState.copy(
                        name = event.name,
                        isNameError = event.name.isNotEmpty() && isLengthError,
                        isNextButtonEnabled = !isLengthError && currentState.selectedAvatarId != null
                    )
                }
            }
            is OnboardingEvent.AvatarSelected -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        selectedAvatarId = event.avatarId,
                        isNextButtonEnabled = currentState.nickname.length in 2..15
                    )
                }
            }
            OnboardingEvent.NextClicked -> {
                saveUserProfile()
            }
        }
    }

    private fun saveUserProfile() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isNextButtonEnabled = false) }

            val currentUser = userRepository.getUser()
            
            val updatedUser = User(
                id = currentUser?.id ?: "",
                email = currentUser?.email,
                nickName = _uiState.value.nickname.trim(),
                roomIds = currentUser?.roomIds ?: emptyList(),
                avatarId = _uiState.value.selectedAvatarId.toString(),
                name = _uiState.value.name.trim(),
                isOnboardingCompleted = true
            )

            saveUserUseCase(updatedUser)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    _effect.send(OnboardingEffect.NavigateToNext)
                }
                .onFailure { exception ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            isNextButtonEnabled = true
                        )
                    }
                    val errorMessage = exception.localizedMessage ?: "Не удалось сохранить профиль"
                    _effect.send(OnboardingEffect.ShowError(errorMessage))
                }
        }
    }

    private fun observeAvatars() {
        _uiState.update { it.copy(isLoading = true) }
        getAvailableAvatarsUseCase()
            .onEach { avatarList ->
                _uiState.update { it.copy(avatars = avatarList, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }
}