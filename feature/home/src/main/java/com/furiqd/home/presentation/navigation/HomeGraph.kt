package com.furiqd.home.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.furiqd.home.presentation.screen.main.MainRoute

fun NavGraphBuilder.homeGraph(navController: NavController) {
    navigation<HomeGraph>(startDestination = MainDestination) {
        composable<MainDestination> {
            MainRoute(
                onNavigateToProfile = { navController.navigate(ProfileDestination) }
            )
        }
    }
}