package com.furiqd.pairing.presentation.screen.joinroom

sealed interface JoinRoomState {
    data class Content(
        val code: String = "",
        val isLoading: Boolean = false,
        val codeLength: Int = 6
    ) : JoinRoomState
}