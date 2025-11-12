package com.anantmittal.ecellkmp.presentation.login_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anantmittal.ecellkmp.domain.repository.EcellRepository
import com.anantmittal.ecellkmp.utility.domain.AppConfig
import com.anantmittal.ecellkmp.utility.domain.AppLogger
import com.anantmittal.ecellkmp.utility.domain.Result
import com.anantmittal.ecellkmp.utility.presentation.UiText
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
                        AppLogger.d(AppConfig.TAG, "LoginVM: Starting login process")
                        _state.update { it.copy(isLoading = true, errorMessage = null) }

                        when (val result = ecellRepository.login(action.loginModel)) {
                            is Result.Success -> {
                                AppLogger.d(AppConfig.TAG, "LoginVM: Login successful for ${result.data.name}")
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        errorMessage = null
                                    )
                                }
                                // Navigation will happen in the UI layer based on success
                            }

                            is Result.Error -> {
                                AppLogger.e(AppConfig.TAG, "LoginVM: Login failed: ${result.error}")
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        errorMessage = UiText.DynamicString("Login failed. Please check your credentials.")
                                    )
                                }
                            }
                        }
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