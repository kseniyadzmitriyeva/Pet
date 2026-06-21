package com.furiqd.home.presentation.screen.main

sealed interface MainEvent {
    object AddRoomClicked : MainEvent
    object RefreshRoom : MainEvent
    object PetClicked : MainEvent
    data class CopyInviteCodeClicked(val code: String) : MainEvent
    object LeaveRoomClicked : MainEvent
    object LogOutClicked : MainEvent
    object InciteFriendClicked : MainEvent
}