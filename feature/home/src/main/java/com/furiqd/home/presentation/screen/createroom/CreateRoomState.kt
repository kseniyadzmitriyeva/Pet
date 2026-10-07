package com.furiqd.home.presentation.screen.createroom

data class CreateRoomState (
    val petName : String = "",
    val petEgg : Int = 1,
    val isLoading : Boolean = false,
    val errorMessage : String? = null
) {

    companion object {
        val petAvatars = listOf(
            com.furiqd.designsystem.R.drawable.green_egg,
            com.furiqd.designsystem.R.drawable.blue_egg,
            com.furiqd.designsystem.R.drawable.orange_egg,
            com.furiqd.designsystem.R.drawable.purple_egg
        )
    }
}