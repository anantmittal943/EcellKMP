package com.anantmittal.ecellkmp.presentation.team_detail_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.anantmittal.ecellkmp.domain.models.AccountModel
import com.anantmittal.ecellkmp.utility.presentation.ColorAccentDark
import com.anantmittal.ecellkmp.utility.presentation.White
import com.anantmittal.ecellkmp.utility.presentation.components.LoadingIndicator
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TeamDetailScreenRoot(
    viewModel: TeamDetailViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TeamDetailScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is TeamDetailAction.OnBackClick -> onBackClick()
            }
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TeamDetailScreen(
    state: TeamDetailState,
    onAction: (TeamDetailAction) -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAccentDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Top App Bar
            TopAppBar(
                title = {
                    Text(
                        text = state.member?.name ?: "Profile",
                        color = White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(TeamDetailAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ColorAccentDark
                ),
                modifier = Modifier
                    .systemBarsPadding()
                    .displayCutoutPadding()
            )

            // Content
            if (state.isLoading && state.member == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator()
                }
            } else if (state.error != null && state.member == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.error,
                        color = White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                state.member?.let { member ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Profile Picture
                        AsyncImage(
                            model = member.profilePic.ifEmpty { "https://via.placeholder.com/150" },
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(Modifier.height(16.dp))

                        // Name
                        Text(
                            text = member.name,
                            color = White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(8.dp))

                        // Designation
                        if (member.designation.isNotEmpty()) {
                            Text(
                                text = member.designation,
                                color = White.copy(alpha = 0.8f),
                                fontSize = 18.sp
                            )
                        }

                        Spacer(Modifier.height(24.dp))

                        // Details Section
                        DetailRow("Email", member.email)
                        DetailRow("Branch", member.branch)
                        DetailRow("Year", member.year)
                        DetailRow("Domain", member.domain)
                        DetailRow("Phone", member.phoneNumber)
                        DetailRow("Library ID", member.kietLibId)

                        if (member.city.isNotEmpty()) {
                            DetailRow("City", member.city)
                        }

                        if (member.linkedinUrl.isNotEmpty()) {
                            DetailRow("LinkedIn", member.linkedinUrl)
                        }

                        if (member.instagramUrl.isNotEmpty()) {
                            DetailRow("Instagram", member.instagramUrl)
                        }

                        if (member.portfolioUrl.isNotEmpty()) {
                            DetailRow("Portfolio", member.portfolioUrl)
                        }

                        Spacer(Modifier.height(20.dp))
                    }
                }
            }
        }

        // Loading overlay when refreshing
        if (state.isLoading && state.member != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        }
    }
}

@Preview
@Composable
fun TeamDetailScreenPreview() {
    val member = AccountModel(
        id = "1",
        name = "Anant Mittal",
        email = "anantmittal943@gmail.com",
        password = "password",
        kietLibId = "2428CS2113",
        branch = "CS",
        profilePic = "https://firebasestorage.googleapis.com/v0/b/e-cell-main-app-68d8c.appspot.com/o/anantmittal943%40gmail.com.jpg?alt=media&token=65d7784b-4352-403d-8da4-0ebab89c22a4",
        accessType = "ADMIN",
        accountType = "Team Member",
        portfolioUrl = "https://firebasestorage.googleapis.com/v0/b/e-cell-main-app-68d8c.appspot.com/o/anantmittal943%40gmail.com.jpg?alt=media&token=65d7784b-4352-403d-8da4-0ebab89c22a4",
        linkedinUrl = "https://www.linkedin.com/in/anantmittal943/",
        instagramUrl = "https://www.instagram.com/anantmittal943/#",
        designation = "Tech Member",
        status = "VERIFIED",
        universityRollNumber = "202401100100041",
        kietEmail = "anant.2428cs2113@kiet.edu",
        accommodationType = "Day Scholar",
        city = "Meerut",
        domain = "Technical",
        year = "2",
        shirtSize = "M"
    )
    TeamDetailScreen(
        state = TeamDetailState(member = member, isLoading = false, error = null),
        onAction = {}
    )
}

@Preview
@Composable
fun TeamDetailScreenLoadingPreview() {
    TeamDetailScreen(
        state = TeamDetailState(member = null, isLoading = true, error = null),
        onAction = {}
    )
}

@Preview
@Composable
fun TeamDetailScreenErrorPreview() {
    TeamDetailScreen(
        state = TeamDetailState(member = null, isLoading = false, error = "Failed to load member details."),
        onAction = {}
    )
}

@Composable
private fun DetailRow(label: String, value: String) {
    if (value.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = label,
                color = White.copy(alpha = 0.6f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = value,
                color = White,
                fontSize = 16.sp
            )
        }
    }
}