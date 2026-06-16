package com.furiqd.home.presentation.screen.main

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun MainScreen(){
}

@Composable
fun MainRoute(
    onNavigateToProfile: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    Text(
        "Main Screen"
    )
}