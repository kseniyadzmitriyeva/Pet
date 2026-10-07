package com.furiqd.home.presentation.screen.main

sealed interface MainEvent {
    object onOpenCreateRoomDialogClicked : MainEvent
    object AddRoomClicked : MainEvent
    object PetClicked : MainEvent
    object ProfileClicked : MainEvent
    object CameraClicked : MainEvent
    data class CopyInviteCodeClicked(val code: String) : MainEvent
    object LeaveRoomClicked : MainEvent
    object LogOutClicked : MainEvent
    object InciteFriendClicked : MainEvent
    object RoomChangedToNextInPager : MainEvent
    object RoomChangedToPreviousInPager : MainEvent
}