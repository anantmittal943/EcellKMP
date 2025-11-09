package com.anantmittal.ecellkmp.presentation.account_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anantmittal.ecellkmp.domain.repository.EcellRepository
import com.anantmittal.ecellkmp.utility.domain.AppLogger
import com.anantmittal.ecellkmp.utility.domain.Variables
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel(
    private val ecellRepository: EcellRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AccountState())
    val state = _state.asStateFlow()

    init {
        observeAccount()
    }

    private fun observeAccount() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            ecellRepository.account.collectLatest { accountModel ->
                if (accountModel != null) {
                    _state.update { it.copy(isLoading = false, account = accountModel) }
                } else {
                    _state.update { it.copy(isLoading = true, account = null) }
                }
            }
        }
    }

//    private fun loadCurrentUserAccount() {
//        viewModelScope.launch {
//            _state.value = _state.value.copy(isLoading = true)
//
//            ecellRepository.currentUser.collect { user ->
//                if (user != null) {
//                    AppLogger.d(Variables.TAG, "Current user: ${user.email}")
//                    loadAccountFromLocal(user.email)
//                } else {
//                    AppLogger.d(Variables.TAG, "No current user found")
//                    _state.value = _state.value.copy(
//                        account = null,
//                        isLoading = false
//                    )
//                }
//            }
//        }
//    }
//
//    private suspend fun loadAccountFromLocal(email: String) {
//        when (val result = ecellRepository.loadAccountLocally(email)) {
//            is Result.Success -> {
//                AppLogger.d(Variables.TAG, "Account loaded successfully: ${result.data.name}")
//                _state.value = _state.value.copy(
//                    account = result.data,
//                    isLoading = false
//                )
//            }
//
//            is Result.Error -> {
//                AppLogger.e(Variables.TAG, "Failed to load account: ${result.error}")
//                _state.value = _state.value.copy(
//                    account = null,
//                    isLoading = false
//                )
//            }
//        }
//    }

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