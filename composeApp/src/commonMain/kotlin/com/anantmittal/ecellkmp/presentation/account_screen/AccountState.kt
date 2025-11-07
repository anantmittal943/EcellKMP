package com.anantmittal.ecellkmp.presentation.account_screen

import com.anantmittal.ecellkmp.domain.models.AccountModel

data class AccountState(
    val account: AccountModel? = null,
    val isLoading: Boolean = false
)
