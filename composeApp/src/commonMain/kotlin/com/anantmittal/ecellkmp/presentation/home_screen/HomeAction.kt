package com.anantmittal.ecellkmp.presentation.home_screen

import com.anantmittal.ecellkmp.domain.models.AccountModel
import com.anantmittal.ecellkmp.domain.models.DomainModel
import com.anantmittal.ecellkmp.domain.models.EventsGlimpsesModel

sealed interface HomeAction {
    data class OnEventGlimpseClick(val eventsGlimpsesModel: EventsGlimpsesModel) : HomeAction
    data class OnDomainClick(val domainModel: DomainModel) : HomeAction
    data class OnTeamMemberClick(val accountModel: AccountModel) : HomeAction
}