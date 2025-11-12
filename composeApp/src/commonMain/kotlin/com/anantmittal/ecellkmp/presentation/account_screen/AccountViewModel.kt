package com.anantmittal.ecellkmp.presentation.account_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anantmittal.ecellkmp.domain.repository.EcellRepository
import com.anantmittal.ecellkmp.utility.domain.AppConfig
import com.anantmittal.ecellkmp.utility.domain.AppLogger
import com.anantmittal.ecellkmp.utility.domain.Result
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
                    AppLogger.d(AppConfig.TAG, "AccountVM: Current user found: ${user.email}")
                    loadAccount(user.email)
                } else {
                    AppLogger.d(AppConfig.TAG, "AccountVM: No current user found")
                    _state.value = _state.value.copy(
                        account = null,
                        isLoading = false
                    )
                }
            }
        }
    }

    private suspend fun loadAccount(email: String) {
        AppLogger.d(AppConfig.TAG, "AccountVM: Loading account for: $email")
        when (val result = ecellRepository.loadAccount(email)) {
            is Result.Success -> {
                AppLogger.d(AppConfig.TAG, "AccountVM: Account loaded successfully: ${result.data.name}")
                _state.value = _state.value.copy(
                    account = result.data,
                    isLoading = false
                )
            }

            is Result.Error -> {
                AppLogger.e(AppConfig.TAG, "AccountVM: Failed to load account: ${result.error}")
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
                AppLogger.d(AppConfig.TAG, "Edit profile clicked")
                // TODO: Navigate to edit profile screen
            }

            is AccountAction.OnLogoutClick -> {
                AppLogger.d(AppConfig.TAG, "Logout clicked")
                viewModelScope.launch {
                    val email = _state.value.account?.email
                    if (email != null) {
                        AppLogger.d(AppConfig.TAG, "Logging out user: $email")
                        ecellRepository.logout(email)
                        _state.value = _state.value.copy(
                            account = null,
                            isLoading = false
                        )
                        AppLogger.d(AppConfig.TAG, "Logout completed")
                    } else {
                        AppLogger.e(AppConfig.TAG, "Cannot logout: No account email found")
                    }
                }
            }

            is AccountAction.OnSocialLinkClick -> {
                AppLogger.d(AppConfig.TAG, "Social link clicked: ${action.url}")
                // TODO: Open URL in browser
            }
        }
    }
}