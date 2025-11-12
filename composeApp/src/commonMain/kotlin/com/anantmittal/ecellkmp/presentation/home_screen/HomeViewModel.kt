package com.anantmittal.ecellkmp.presentation.home_screen

import androidx.lifecycle.ViewModel
import com.anantmittal.ecellkmp.utility.domain.AppConfig
import com.anantmittal.ecellkmp.utility.domain.AppLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnDomainClick -> {
                AppLogger.d(AppConfig.TAG, "Domain clicked: ${action.domainModel}")
                // TODO: Navigate to domain details
            }

            is HomeAction.OnEventGlimpseClick -> {
                AppLogger.d(AppConfig.TAG, "Event clicked: ${action.eventsGlimpsesModel}")
                // TODO: Navigate to event details
            }

            is HomeAction.OnTeamMemberClick -> {
                AppLogger.d(AppConfig.TAG, "Team member clicked: ${action.profile.name}")
                // TODO: Navigate to team member profile
            }

            HomeAction.OnViewAllTeamMembersClick -> {
                AppLogger.d(AppConfig.TAG, "View all team members clicked")
                // TODO: Navigate to all team members screen
            }
        }
    }
}