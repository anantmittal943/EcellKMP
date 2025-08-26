package com.anantmittal.ecellkmp.presentation.home_screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnDomainClick -> TODO()
            is HomeAction.OnEventGlimpseClick -> TODO()
            is HomeAction.OnTeamMemberClick -> TODO()
        }
    }
}