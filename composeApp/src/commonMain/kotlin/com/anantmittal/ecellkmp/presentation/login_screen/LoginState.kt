package com.anantmittal.ecellkmp.presentation.login_screen

import com.anantmittal.ecellkmp.utility.presentation.UiText

data class LoginState(
    val email: String = "",
    val emailError: String? = null,

    val password: String = "",
    val passwordError: String? = null,

    val isVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null,
    val isLoginButtonEnabled: Boolean = false
)
