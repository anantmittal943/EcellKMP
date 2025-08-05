package com.anantmittal.ecellkmp.presentation.login_screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnEmailChange -> {
                _state.update {
                    it.copy(email = action.email)
                }
            }

            is LoginAction.OnPasswordChange -> {
                _state.update {
                    it.copy(password = action.password)
                }
            }

            is LoginAction.OnVisibilityChange -> {
                _state.update {
                    it.copy(isVisible = action.isVisible)
                }
            }

            is LoginAction.OnLoginClick -> TODO()
            is LoginAction.OnSignupClick -> {}
        }
    }
}