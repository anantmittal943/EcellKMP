package com.anantmittal.ecellkmp.presentation.account_screen

sealed interface AccountAction {
    data object OnEditProfileClick : AccountAction
    data object OnLogoutClick : AccountAction
    data class OnSocialLinkClick(val url: String) : AccountAction
}