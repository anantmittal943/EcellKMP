package com.anantmittal.ecellkmp.presentation.home_screen

import com.anantmittal.ecellkmp.domain.models.AccountModel
import com.anantmittal.ecellkmp.domain.models.DomainModel
import com.anantmittal.ecellkmp.domain.models.EventsGlimpsesModel
import com.anantmittal.ecellkmp.utility.presentation.UiText

data class HomeState(
    val eventsGlimpses: List<EventsGlimpsesModel> = emptyList(),
    val domainsList: List<DomainModel> = emptyList(),
    val teamMembers: List<AccountModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null
)
