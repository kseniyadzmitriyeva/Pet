package com.furiqd.auth.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.furiqd.auth.presentation.screens.signin.SignInRoute
import com.furiqd.auth.presentation.screens.signup.SignUpRoute
import com.furiqd.auth.presentation.screens.start.StartRoute
import com.furiqd.auth.presentation.screens.welcome.WelcomeRoute

fun NavGraphBuilder.authGraph(navController: NavController,
                              onAuthSuccess: () -> Unit) {
    navigation<AuthGraph>(startDestination = StartDestination) {

        composable<StartDestination> {
            StartRoute(
                onNavigateToSignUp = { navController.navigate(SignUpDestination) },
                onNavigateToLogin = { navController.navigate(SignInDestination) }
            )
        }

        composable<SignUpDestination> {
            SignUpRoute(
                onNavigateToWelcome = {
                    navController.navigate("home_graph") {
                        popUpTo(AuthGraph) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSignIn = { navController.navigate(SignInDestination) }
            )
        }

        composable<SignInDestination> {
            SignInRoute(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSignUp = { navController.navigate(SignUpDestination) },
                onNavigateToForgotPassword = { navController.navigate(ForgotPasswordDestination) },
                onNavigateToWelcome = {
                    navController.navigate("home_graph") {
                        popUpTo(AuthGraph) { inclusive = true }
                    }
                }
            )
        }

        composable<ForgotPasswordDestination> {
            /*ForgotPasswordRoute(
                onNavigateBack = { navController.popBackStack() }
            )*/
        }

        composable<WelcomeDestination> {
            WelcomeRoute(
                onGetStartedClicked = onAuthSuccess
            )
        }
    }
}