package com.furiqd.personalisation.presentation.screen.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furiqd.personalisation.domain.GetAvatarsUseCase
import com.furiqd.personalisation.domain.SaveUserUseCase
import com.furiqd.shared.model.User
import com.furiqd.shared.repository.UserRepository
import com.furiqd.shared.usecase.UpdateAvatarUseCase
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
    private val userRepository: UserRepository,
    private val updateAvatarUseCase: UpdateAvatarUseCase
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
                    val isError = event.nickname.isNotEmpty() && event.nickname.length !in 2..15
                    validateAndCopy(
                        currentState.copy(
                            nickname = event.nickname,
                            isNicknameError = isError
                        )
                    )
                }
            }

            is OnboardingEvent.NameChanged -> {
                _uiState.update { currentState ->
                    val isError = event.name.isNotEmpty() && event.name.length !in 2..15
                    validateAndCopy(
                        currentState.copy(
                            name = event.name,
                            isNameError = isError
                        )
                    )
                }
            }

            is OnboardingEvent.UploadCustomAvatarClicked -> {
                viewModelScope.launch {
                    _effect.send(OnboardingEffect.UploadAvatar)
                }
            }

            is OnboardingEvent.CustomAvatarSelected -> {
                _uiState.update { currentState ->
                    validateAndCopy(
                        currentState.copy(
                            localAvatarUri = event.uri,
                            selectedCustomBytes = event.bytes
                        )
                    )
                }
            }
            OnboardingEvent.NextClicked -> {
                saveUserProfile()
            }
        }
    }
    private fun validateAndCopy(state: OnboardingState): OnboardingState {
        val isNicknameValid = state.nickname.trim().length in 2..15
        val isNameValid = state.name.trim().length in 2..15
        val hasAvatar = state.selectedCustomBytes != null

        return state.copy(
            isNextButtonEnabled = isNicknameValid && isNameValid && hasAvatar
        )
    }

    private fun saveUserProfile() {
        if (_uiState.value.isLoading || !_uiState.value.isNextButtonEnabled) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isNextButtonEnabled = false) }

            val currentState = _uiState.value

            val finalAvatarUrl: String? = currentState.selectedCustomBytes?.let { bytes ->
                val uploadResult = updateAvatarUseCase(bytes)
                if (uploadResult.isFailure) {
                    _uiState.update { validateAndCopy(it.copy(isLoading = false)) }
                    _effect.send(OnboardingEffect.ShowError("Не удалось загрузить аватарку"))
                    return@launch
                }
                uploadResult.getOrNull()
            }

            val currentUser = userRepository.getUser()
            val updatedUser = User(
                id = currentUser?.id.orEmpty(),
                email = currentUser?.email,
                nickName = currentState.nickname.trim(),
                roomIds = currentUser?.roomIds.orEmpty(),
                avatarId = finalAvatarUrl,
                name = currentState.name.trim(),
                isOnboardingCompleted = true
            )

            saveUserUseCase(updatedUser)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    _effect.send(OnboardingEffect.NavigateToNext)
                }
                .onFailure { exception ->
                    _uiState.update { validateAndCopy(it.copy(isLoading = false)) }
                    _effect.send(
                        OnboardingEffect.ShowError(
                            exception.localizedMessage ?: "Ошибка сохранения"
                        )
                    )
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