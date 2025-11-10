package com.anantmittal.ecellkmp.presentation.home_screen

import androidx.lifecycle.ViewModel
import com.anantmittal.ecellkmp.utility.domain.AppLogger
import com.anantmittal.ecellkmp.utility.domain.Variables
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnDomainClick -> {
                AppLogger.d(Variables.TAG, "Domain clicked: ${action.domainModel}")
                // TODO: Navigate to domain details
            }

            is HomeAction.OnEventGlimpseClick -> {
                AppLogger.d(Variables.TAG, "Event clicked: ${action.eventsGlimpsesModel}")
                // TODO: Navigate to event details
            }

            is HomeAction.OnTeamMemberClick -> {
                AppLogger.d(Variables.TAG, "Team member clicked: ${action.profile.name}")
                // TODO: Navigate to team member profile
            }

            HomeAction.OnViewAllTeamMembersClick -> {
                AppLogger.d(Variables.TAG, "View all team members clicked")
                // TODO: Navigate to all team members screen
            }
        }
    }
}