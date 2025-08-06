package com.anantmittal.ecellkmp.presentation.signup_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anantmittal.ecellkmp.domain.repository.EcellRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignupViewModel(
    private val ecellRepository: EcellRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SignupState())
    val state = _state.asStateFlow()

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
                viewModelScope.launch {
                    ecellRepository.signup(action.signupModel)
                }
            }
        }
    }
}