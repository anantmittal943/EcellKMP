package com.anantmittal.ecellkmp.presentation.view_profile_screen

import com.anantmittal.ecellkmp.domain.models.AccountModel

sealed interface ViewProfileAction {
    data class OnSelectedMemberChange(val profile: AccountModel) : ViewProfileAction
}