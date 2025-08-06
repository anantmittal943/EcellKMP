package com.anantmittal.ecellkmp.presentation.login_screen

import com.anantmittal.ecellkmp.utility.presentation.UiText

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null
)
