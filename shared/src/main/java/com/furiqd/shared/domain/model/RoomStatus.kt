package com.furiqd.shared.domain.model

sealed interface RoomStatus {
    object WaitingForFriend : RoomStatus
    object Active : RoomStatus
    object NotFound : RoomStatus
}