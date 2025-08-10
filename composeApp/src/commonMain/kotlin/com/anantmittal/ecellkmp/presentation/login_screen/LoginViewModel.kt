package com.anantmittal.ecellkmp.presentation.login_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anantmittal.ecellkmp.domain.repository.EcellRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val ecellRepository: EcellRepository
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    init {
        observer()
    }

    @OptIn(FlowPreview::class)
    private fun observer() {
        viewModelScope.launch {
            state
                .debounce(500L)
                .collect { currentState ->
                    validateForm(currentState)
                }
        }
    }

    private fun validateForm(state: LoginState) {
        val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        val emailError = when {
            state.email.isBlank() && state.emailIsTouched -> "Email cannot be empty"
            !state.email.matches(emailRegex) && state.emailIsTouched -> "Invalid email format"
            else -> null
        }

        val passwordError = when {
            state.password.isBlank() && state.passwordIsTouched -> "Password cannot be empty"
            state.password.length < 8 && state.passwordIsTouched -> "Password must be at least 8 characters"
            else -> null
        }

        val isButtonEnabled = emailError == null && passwordError == null

        _state.update {
            it.copy(
                emailError = emailError,
                passwordError = passwordError,
                isLoginButtonEnabled = isButtonEnabled
            )
        }
    }

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

            is LoginAction.OnLoginClick -> {
                _state.update {
                    it.copy(
                        emailIsTouched = true,
                        passwordIsTouched = true
                    )
                }
                val currentState = _state.value
                validateForm(currentState)
                if (_state.value.isLoginButtonEnabled) {
                    viewModelScope.launch {
                        ecellRepository.login(action.loginModel)
                    }
                }
            }

            is LoginAction.OnSignupClick -> {}

            LoginAction.OnEmailFocusLost -> {
                _state.update {
                    it.copy(
                        emailIsTouched = true
                    )
                }
            }

            LoginAction.OnPasswordFocusLost -> {
                _state.update {
                    it.copy(
                        passwordIsTouched = true
                    )
                }
            }
        }
    }
}