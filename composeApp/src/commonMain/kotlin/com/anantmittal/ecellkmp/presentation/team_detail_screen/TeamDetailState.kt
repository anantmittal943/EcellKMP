package com.anantmittal.ecellkmp.presentation.team_detail_screen

import com.anantmittal.ecellkmp.domain.models.AccountModel

data class TeamDetailState(
    val member: AccountModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

