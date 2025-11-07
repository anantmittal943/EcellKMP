package com.anantmittal.ecellkmp.presentation.home_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anantmittal.ecellkmp.presentation.home_screen.components.EventGlimpseBanner
import com.anantmittal.ecellkmp.presentation.home_screen.components.TeamMembersList
import com.anantmittal.ecellkmp.utility.presentation.ColorAccentDark
import com.anantmittal.ecellkmp.utility.presentation.White
import ecellkmp.composeapp.generated.resources.Res
import ecellkmp.composeapp.generated.resources.overlockreg
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    HomeScreen(
        state = state,
        onAction = { action -> viewModel.onAction(action) }
    )
}

@Composable
private fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .background(ColorAccentDark)
            .systemBarsPadding()
            .displayCutoutPadding()
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = "Ecell",
            textAlign = TextAlign.Center,
            color = White,
            fontSize = 42.sp,
            fontFamily = FontFamily(Font(Res.font.overlockreg))
        )
        Spacer(Modifier.height(14.dp))

        EventGlimpseBanner(
            modifier = Modifier
        )

//        Spacer(Modifier.height(14.dp))

//        ProfileCard(
//            modifier = Modifier
//        )

        Spacer(Modifier.height(14.dp))

        // Team Members Section
        if (state.teamMembers.isNotEmpty()) {
            TeamMembersList(
                teamMembers = state.teamMembers,
                onTeamMemberClick = { accountModel ->
                    onAction(HomeAction.OnTeamMemberClick(accountModel))
                },
                onViewAllClick = {
                    onAction(HomeAction.OnViewAllTeamMembersClick)
                },
                modifier = Modifier,
                showViewAllButton = true,
                itemsToShow = 6 // Show only 6 items initially
            )
        }

        Spacer(Modifier.height(20.dp))
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        state = HomeState(),
        onAction = {}
    )
}