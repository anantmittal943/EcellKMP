package com.anantmittal.ecellkmp.presentation.signup_screen

import com.anantmittal.ecellkmp.utility.presentation.UiText

data class SignupState(
    val name: String = "",
    val email: String = "",
    val kietLibId: String = "",
    val password: String = "",
    val cnfmPassword: String = "",
    val isVisible: Boolean = true,
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null
)
