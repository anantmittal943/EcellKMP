package com.anantmittal.ecellkmp.presentation.team_shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anantmittal.ecellkmp.domain.models.AccountModel
import com.anantmittal.ecellkmp.domain.repository.EcellRepository
import com.anantmittal.ecellkmp.utility.domain.AppLogger
import com.anantmittal.ecellkmp.utility.domain.Result
import com.anantmittal.ecellkmp.utility.domain.Variables
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TeamSharedViewModel(
    private val ecellRepository: EcellRepository
) : ViewModel() {
    private val _state = MutableStateFlow(TeamSharedState())
    val state = _state.asStateFlow()

    fun selectMember(member: AccountModel?) {
        AppLogger.d(Variables.TAG, "Member selected: ${member?.name}")
        _state.update { it.copy(selectedMember = member) }
    }

    fun loadFullTeamList() {
        if (state.value.fullTeamList.isNotEmpty()) return
        AppLogger.d(Variables.TAG, "Loading full team list...")
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = ecellRepository.loadTeamAccountsRemotely()) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            fullTeamList = result.data,
                            isLoading = false
                        )
                    }
                }

                is Result.Error -> {
                    AppLogger.e(Variables.TAG, "Failed to load full team list: ${result.error}")
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }
}