package com.anantmittal.ecellkmp.presentation.team_shared

import com.anantmittal.ecellkmp.domain.models.AccountModel

data class TeamSharedState(
    val fullTeamList: List<AccountModel> = emptyList(),
    val selectedMember: AccountModel? = null,
    val isLoading: Boolean = false
)