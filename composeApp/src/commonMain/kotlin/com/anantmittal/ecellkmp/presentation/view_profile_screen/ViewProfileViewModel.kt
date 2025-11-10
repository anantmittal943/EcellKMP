package com.anantmittal.ecellkmp.presentation.view_profile_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ViewProfileViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(ViewProfileState())
    val state = _state.asStateFlow()

    fun onAction(action: ViewProfileAction) {
        when (action) {
            is ViewProfileAction.OnSelectedMemberChange -> {
                _state.update {
                    it.copy(
                        profile = action.profile
                    )
                }
            }
        }
    }

}