package com.anantmittal.ecellkmp.presentation.account_screen

import androidx.lifecycle.ViewModel
import com.anantmittal.ecellkmp.utility.domain.AppLogger
import com.anantmittal.ecellkmp.utility.domain.Variables
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AccountViewModel : ViewModel() {
    private val _state = MutableStateFlow(AccountState())
    val state = _state.asStateFlow()

    fun onAction(action: AccountAction) {
        when (action) {
            AccountAction.OnEditProfileClick -> {
                AppLogger.d(Variables.TAG, "Edit profile clicked")
                // TODO: Navigate to edit profile screen
            }

            AccountAction.OnLogoutClick -> {
                AppLogger.d(Variables.TAG, "Logout clicked")
                // TODO: Implement logout logic
            }

            is AccountAction.OnSocialLinkClick -> {
                AppLogger.d(Variables.TAG, "Social link clicked: ${action.url}")
                // TODO: Open URL in browser
            }
        }
    }
}