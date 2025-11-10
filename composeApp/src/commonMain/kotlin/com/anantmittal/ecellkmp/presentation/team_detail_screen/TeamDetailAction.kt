package com.anantmittal.ecellkmp.presentation.team_detail_screen

sealed interface TeamDetailAction {
    data object OnBackClick : TeamDetailAction
}

