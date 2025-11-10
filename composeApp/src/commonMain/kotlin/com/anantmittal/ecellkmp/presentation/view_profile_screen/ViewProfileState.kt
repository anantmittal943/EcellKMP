package com.anantmittal.ecellkmp.presentation.view_profile_screen

import com.anantmittal.ecellkmp.domain.models.AccountModel

data class ViewProfileState(
    val profile: AccountModel? = null,
    val isLoading: Boolean = false
)