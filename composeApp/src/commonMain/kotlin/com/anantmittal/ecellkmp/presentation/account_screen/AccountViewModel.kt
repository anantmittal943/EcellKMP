package com.anantmittal.ecellkmp.presentation.account_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anantmittal.ecellkmp.domain.repository.EcellRepository
import com.anantmittal.ecellkmp.utility.domain.AppLogger
import com.anantmittal.ecellkmp.utility.domain.Result
import com.anantmittal.ecellkmp.utility.domain.Variables
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AccountViewModel(
    private val ecellRepository: EcellRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AccountState())
    val state = _state.asStateFlow()

    init {
        loadCurrentUserAccount()
    }

    private fun loadCurrentUserAccount() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            ecellRepository.currentUser.collect { user ->
                if (user != null) {
                    AppLogger.d(Variables.TAG, "AccountVM: Current user found: ${user.email}")
                    loadAccount(user.email)
                } else {
                    AppLogger.d(Variables.TAG, "AccountVM: No current user found")
                    _state.value = _state.value.copy(
                        account = null,
                        isLoading = false
                    )
                }
            }
        }
    }

    /**
     * Loads account using the repository's local-first strategy
     * This automatically checks local cache first, then falls back to remote if needed
     * Runs in background (viewModelScope) without blocking UI
     */
    private suspend fun loadAccount(email: String) {
        AppLogger.d(Variables.TAG, "AccountVM: Loading account for: $email")
        when (val result = ecellRepository.loadAccount(email)) {
            is Result.Success -> {
                AppLogger.d(Variables.TAG, "AccountVM: Account loaded successfully: ${result.data.name}")
                _state.value = _state.value.copy(
                    account = result.data,
                    isLoading = false
                )
            }

            is Result.Error -> {
                AppLogger.e(Variables.TAG, "AccountVM: Failed to load account: ${result.error}")
                _state.value = _state.value.copy(
                    account = null,
                    isLoading = false
                )
            }
        }
    }

    fun onAction(action: AccountAction) {
        when (action) {
            is AccountAction.OnEditProfileClick -> {
                AppLogger.d(Variables.TAG, "Edit profile clicked")
                // TODO: Navigate to edit profile screen
            }

            is AccountAction.OnLogoutClick -> {
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