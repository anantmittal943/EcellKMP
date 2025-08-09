package com.anantmittal.ecellkmp.presentation.signup_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anantmittal.ecellkmp.domain.repository.EcellRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignupViewModel(
    private val ecellRepository: EcellRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SignupState())
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

    private fun validateForm(state: SignupState) {
        val nameError = if (state.name.isBlank() && state.nameIsTouched) "Name cannot be empty" else null

        val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        val emailError = when {
            state.email.isBlank() && state.emailIsTouched -> "Email cannot be empty"
            !state.email.matches(emailRegex) && state.emailIsTouched -> "Invalid email format"
            else -> null
        }

        val kietLibIdError = if (state.kietLibId.isBlank() && state.kietLibIdIsTouched) "Library ID cannot be empty" else null

        val passwordError = when {
            state.password.isBlank() && state.passwordIsTouched -> "Password cannot be empty"
            state.password.length < 8 && state.passwordIsTouched -> "Password must be at least 8 characters"
            else -> null
        }

        val cnfmPasswordError = when {
            state.cnfmPassword.isBlank() && state.cnfmPasswordIsTouched -> "Please confirm your password"
            state.cnfmPassword != state.password && state.cnfmPasswordIsTouched -> "Passwords do not match"
            else -> null
        }

        val isButtonEnabled = nameError == null && emailError == null && kietLibIdError == null &&
                passwordError == null && cnfmPasswordError == null

        _state.update {
            it.copy(
                nameError = nameError,
                emailError = emailError,
                kietLibIdError = kietLibIdError,
                passwordError = passwordError,
                cnfmPasswordError = cnfmPasswordError,
                isSignupButtonEnabled = isButtonEnabled
            )
        }
    }

    fun onAction(action: SignupAction) {
        when (action) {
            is SignupAction.OnNameChange -> {
                _state.update {
                    it.copy(name = action.name)
                }
            }

            is SignupAction.OnEmailChange -> {
                _state.update {
                    it.copy(email = action.email)
                }
            }

            is SignupAction.OnKietLibIdChange ->
                _state.update {
                    it.copy(kietLibId = action.kietLibId)
                }

            is SignupAction.OnPasswordChange -> {
                _state.update {
                    it.copy(password = action.password)
                }
            }

            is SignupAction.OnCnfmPasswordChange -> {
                _state.update {
                    it.copy(cnfmPassword = action.cnfmPassword)
                }
            }

            is SignupAction.OnVisibilityChange -> {
                _state.update {
                    it.copy(isVisible = action.isVisible)
                }
            }

            is SignupAction.OnSignupClick -> {
                _state.update {
                    it.copy(
                        nameIsTouched = true,
                        emailIsTouched = true,
                        kietLibIdIsTouched = true,
                        passwordIsTouched = true,
                        cnfmPasswordIsTouched = true
                    )
                }
                val currentState = _state.value
                validateForm(currentState)
                if (_state.value.isSignupButtonEnabled) {
                    viewModelScope.launch {
                        ecellRepository.signup(action.signupModel)
                    }
                }
            }

            SignupAction.OnNameFocusLost -> {
                _state.update {
                    it.copy(nameIsTouched = true)
                }
            }

            SignupAction.OnEmailFocusLost -> {
                _state.update {
                    it.copy(emailIsTouched = true)
                }
            }

            SignupAction.OnKietLibIdFocusLost -> {
                _state.update {
                    it.copy(kietLibIdIsTouched = true)
                }
            }

            SignupAction.OnPasswordFocusLost -> {
                _state.update {
                    it.copy(passwordIsTouched = true)
                }
            }

            SignupAction.OnCnfmPasswordFocusLost -> {
                _state.update {
                    it.copy(cnfmPasswordIsTouched = true)
                }
            }
        }
    }
}