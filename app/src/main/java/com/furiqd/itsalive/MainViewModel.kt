package com.furiqd.itsalive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furiqd.data.models.UserSessionState
import com.furiqd.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {
    val sessionState: StateFlow<UserSessionState> = userRepository.sessionState
}