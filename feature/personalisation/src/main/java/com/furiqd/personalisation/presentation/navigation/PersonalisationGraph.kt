package com.furiqd.personalisation.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.furiqd.personalisation.presentation.screen.welcome.WelcomeRoute


fun NavGraphBuilder.personalisationGraph(navController: NavController,
                                         onPersonalisationComplete: () -> Unit) {
    navigation<PersonalisationGraph>(startDestination = WelcomeDestination) {

        composable<WelcomeDestination> {
            WelcomeRoute(
                onNavigateToOnboarding = { navController.navigate(OnboardingDestination) }
            )
        }
    }
}