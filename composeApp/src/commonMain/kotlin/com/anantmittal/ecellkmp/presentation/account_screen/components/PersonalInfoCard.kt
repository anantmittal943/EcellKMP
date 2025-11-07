package com.anantmittal.ecellkmp.presentation.account_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anantmittal.ecellkmp.domain.models.AccountModel
import com.anantmittal.ecellkmp.utility.presentation.ColorPrimaryLight
import com.anantmittal.ecellkmp.utility.presentation.White
import ecellkmp.composeapp.generated.resources.Res
import ecellkmp.composeapp.generated.resources.main_font
import org.jetbrains.compose.resources.Font

@Composable
fun PersonalInfoCard(
    account: AccountModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = ColorPrimaryLight
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Personal Information",
                color = White,
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(Res.font.main_font)),
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            InfoRow(label = "Email", value = account.email)
            InfoRow(label = "KIET Email", value = account.kietEmail)
            InfoRow(label = "Phone", value = account.phoneNumber?.toString() ?: "Not provided")
            InfoRow(label = "City", value = account.city.ifEmpty { "Not provided" })
            InfoRow(label = "Account Type", value = account.accountType)
            InfoRow(label = "Access Level", value = account.accessType, isLast = true)
        }
    }
}

