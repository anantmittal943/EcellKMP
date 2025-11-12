package com.anantmittal.ecellkmp.presentation.team_detail_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.anantmittal.ecellkmp.app.Route
import com.anantmittal.ecellkmp.domain.repository.EcellRepository
import com.anantmittal.ecellkmp.presentation.team_shared.TeamSharedViewModel
import com.anantmittal.ecellkmp.utility.domain.AppConfig
import com.anantmittal.ecellkmp.utility.domain.AppLogger
import com.anantmittal.ecellkmp.utility.domain.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TeamDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val ecellRepository: EcellRepository,
    private val teamSharedViewModel: TeamSharedViewModel
) : ViewModel() {

    private val _state = MutableStateFlow(TeamDetailState())
    val state = _state.asStateFlow()

    private val memberId: String

    init {
        val args = savedStateHandle.toRoute<Route.TeamDetail>()
        memberId = args.email
        AppLogger.d(AppConfig.TAG, "TeamDetailViewModel initialized for member ID: $memberId")

        loadMemberDetails()
    }

    private fun loadMemberDetails() {
        viewModelScope.launch {
            val cachedMember = teamSharedViewModel.state.value.selectedMember
            if (cachedMember != null && cachedMember.email == memberId) {
                AppLogger.d(AppConfig.TAG, "Using cached member data from shared ViewModel: ${cachedMember.name}")
                _state.update {
                    it.copy(
                        member = cachedMember,
                        isLoading = false
                    )
                }
            }

            AppLogger.d(AppConfig.TAG, "Fetching fresh member data for ID: $memberId")
            _state.update { it.copy(isLoading = true) }

            when (val result = ecellRepository.loadAccount(memberId)) {
                is Result.Success -> {
                    AppLogger.d(AppConfig.TAG, "Successfully loaded member: ${result.data.name}")
                    _state.update {
                        it.copy(
                            member = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                }

                is Result.Error -> {
                    AppLogger.e(AppConfig.TAG, "Failed to load member details: ${result.error}")
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "Failed to load member details"
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: TeamDetailAction) {
        when (action) {
            is TeamDetailAction.OnBackClick -> {}
        }
    }
}

