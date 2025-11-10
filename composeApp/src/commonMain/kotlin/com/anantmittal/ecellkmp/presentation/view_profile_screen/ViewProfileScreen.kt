package com.anantmittal.ecellkmp.presentation.view_profile_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ViewProfileScreenRoot(
    viewModel: ViewProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ViewProfileScreen(
        state = state,
        onAction = { action -> viewModel.onAction(action) }
    )
}

@Composable
fun ViewProfileScreen(
    state: ViewProfileState,
    onAction: (ViewProfileAction) -> Unit
) {
}