package com.furiqd.personalisation.presentation.screen.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furiqd.personalisation.domain.GetAvatarsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val getAvailableAvatarsUseCase: GetAvatarsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingState())
    val uiState = _uiState.asStateFlow()

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
            is OnboardingEvent.AvatarSelected -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        selectedAvatarId = event.avatarId,
                        isNextButtonEnabled = currentState.nickname.length in 2..15
                    )
                }
            }
            OnboardingEvent.NextClicked -> {
                // TODO: Навигация или сохранение в репозиторий
            }
        }
    }

    private fun observeAvatars() {
        getAvailableAvatarsUseCase()
            .onEach { avatarList ->
                _uiState.update { it.copy(avatars = avatarList) }
            }
            .launchIn(viewModelScope)
    }
}