package com.furiqd.pairing.presentation.screen.joinroom

sealed interface JoinRoomEvent {
    data class CodeChanged(val code: String) : JoinRoomEvent
    data object SubmitClicked : JoinRoomEvent
    data object BackClicked : JoinRoomEvent
}