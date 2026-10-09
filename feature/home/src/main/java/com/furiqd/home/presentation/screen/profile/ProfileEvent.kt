package com.furiqd.home.presentation.screen.profile


interface ProfileEvent {
    data object AvatarClicked : ProfileEvent
    data object LogoutClicked : ProfileEvent
    data object BackClicked : ProfileEvent
    data object EditProfileClicked : ProfileEvent
    class AvatarSelected(val bytes: ByteArray) : ProfileEvent {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as AvatarSelected

            return bytes.contentEquals(other.bytes)
        }

        override fun hashCode(): Int {
            return bytes.contentHashCode()
        }
    }
}