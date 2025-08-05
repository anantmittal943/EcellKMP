package com.anantmittal.ecellkmp.presentation.login_screen

import com.anantmittal.ecellkmp.domain.models.LoginModel

sealed interface LoginAction {
    data class OnEmailChange(val email: String) : LoginAction
    data class OnPasswordChange(val password: String) : LoginAction
    data class OnVisibilityChange(val isVisible: Boolean) : LoginAction
    data class OnLoginClick(val loginModel: LoginModel) : LoginAction
    data object OnSignupClick : LoginAction
}