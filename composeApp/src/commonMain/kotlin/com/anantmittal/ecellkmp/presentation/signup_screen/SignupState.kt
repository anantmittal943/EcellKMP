package com.anantmittal.ecellkmp.presentation.signup_screen

import com.anantmittal.ecellkmp.utility.presentation.UiText

data class SignupState(
    val name: String = "",
    val nameError: String? = null,
    val nameIsTouched: Boolean = false,

    val email: String = "",
    val emailError: String? = null,
    val emailIsTouched: Boolean = false,

    val kietLibId: String = "",
    val kietLibIdError: String? = null,
    val kietLibIdIsTouched: Boolean = false,

    val password: String = "",
    val passwordError: String? = null,
    val passwordIsTouched: Boolean = false,

    val cnfmPassword: String = "",
    val cnfmPasswordError: String? = null,
    val cnfmPasswordIsTouched: Boolean = false,

    val isVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null,
    val isSignupButtonEnabled: Boolean = false
)