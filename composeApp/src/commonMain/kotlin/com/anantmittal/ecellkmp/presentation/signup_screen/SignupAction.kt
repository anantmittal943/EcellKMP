package com.anantmittal.ecellkmp.presentation.signup_screen

import com.anantmittal.ecellkmp.domain.models.SignupModel

sealed interface SignupAction {
    data class OnNameChange(val name: String) : SignupAction
    data class OnEmailChange(val email: String) : SignupAction
    data class OnKietLibIdChange(val kietLibId: String) : SignupAction
    data class OnPasswordChange(val password: String) : SignupAction
    data class OnCnfmPasswordChange(val cnfmPassword: String) : SignupAction
    data class OnVisibilityChange(val isVisible: Boolean) : SignupAction
    data class OnSignupClick(val signupModel: SignupModel) : SignupAction
}