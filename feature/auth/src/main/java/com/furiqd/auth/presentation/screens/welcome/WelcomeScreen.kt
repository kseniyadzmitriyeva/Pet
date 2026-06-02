package com.furiqd.auth.presentation.screens.welcome

import androidx.compose.runtime.Composable
@Composable
fun WelcomeScreen(
    onGetStartedClicked: () -> Unit,
) {

}

@Composable
fun WelcomeRoute(onGetStartedClicked: () -> Unit){
    WelcomeScreen(
        onGetStartedClicked = onGetStartedClicked
    )
}