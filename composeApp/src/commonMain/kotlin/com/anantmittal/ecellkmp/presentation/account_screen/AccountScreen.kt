package com.anantmittal.ecellkmp.presentation.account_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anantmittal.ecellkmp.domain.models.AccountModel
import com.anantmittal.ecellkmp.presentation.account_screen.components.AcademicInfoCard
import com.anantmittal.ecellkmp.presentation.account_screen.components.AdditionalInfoCard
import com.anantmittal.ecellkmp.presentation.account_screen.components.PersonalInfoCard
import com.anantmittal.ecellkmp.presentation.account_screen.components.ProfileHeader
import com.anantmittal.ecellkmp.presentation.account_screen.components.SocialLinksCard
import com.anantmittal.ecellkmp.utility.presentation.ColorAccent
import com.anantmittal.ecellkmp.utility.presentation.ColorAccentDark
import com.anantmittal.ecellkmp.utility.presentation.ColorAccentLightest
import com.anantmittal.ecellkmp.utility.presentation.ErrorRed
import com.anantmittal.ecellkmp.utility.presentation.White
import ecellkmp.composeapp.generated.resources.Res
import ecellkmp.composeapp.generated.resources.main_font
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AccountScreenRoot(
    viewModel: AccountViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AccountScreen(
        state = state,
        onAction = { action -> viewModel.onAction(action) }
    )
}

@Composable
private fun AccountScreen(
    state: AccountState,
    onAction: (AccountAction) -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAccentDark)
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = ColorAccent
            )
        } else if (state.account != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // Header with gradient background
                ProfileHeader(
                    account = state.account,
                    onEditClick = { onAction(AccountAction.OnEditProfileClick) }
                )

                Spacer(Modifier.height(16.dp))

                // Personal Information Card
                PersonalInfoCard(account = state.account)

                Spacer(Modifier.height(12.dp))

                // Academic Information Card
                AcademicInfoCard(account = state.account)

                Spacer(Modifier.height(12.dp))

                // Social Links Card (if available)
                if (state.account.linkedinUrl.isNotEmpty() ||
                    state.account.instagramUrl.isNotEmpty() ||
                    state.account.portfolioUrl.isNotEmpty()
                ) {
                    SocialLinksCard(
                        account = state.account,
                        onLinkClick = { url -> onAction(AccountAction.OnSocialLinkClick(url)) }
                    )
                    Spacer(Modifier.height(12.dp))
                }

                // Additional Info Card
                AdditionalInfoCard(account = state.account)

                Spacer(Modifier.height(16.dp))

                // Logout Button
                Button(
                    onClick = { onAction(AccountAction.OnLogoutClick) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ErrorRed
                    )
                ) {
                    Text(
                        text = "Logout",
                        color = White,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(Res.font.main_font)),
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(Modifier.height(24.dp))
            }
        } else {
            // No account loaded
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No account information available",
                    color = ColorAccentLightest,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(Res.font.main_font))
                )
            }
        }
    }
}

@Preview
@Composable
private fun AccountScreenPreview() {
    val account = AccountModel(
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
    val state = AccountState(
        account = account,
        isLoading = false
    )
    AccountScreen(state = state, onAction = {})
}

@Preview
@Composable
private fun AccountScreenNoDataPreview() {
    val state = AccountState(
        account = null,
        isLoading = false
    )
    AccountScreen(state = state, onAction = {})
}


