package com.anantmittal.ecellkmp.presentation.account_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anantmittal.ecellkmp.domain.models.AccountModel
import com.anantmittal.ecellkmp.utility.presentation.ColorAccent
import com.anantmittal.ecellkmp.utility.presentation.ColorAccentLight
import com.anantmittal.ecellkmp.utility.presentation.ColorAccentLightest
import com.anantmittal.ecellkmp.utility.presentation.ColorPrimaryLightest
import com.anantmittal.ecellkmp.utility.presentation.ErrorRed
import com.anantmittal.ecellkmp.utility.presentation.White
import ecellkmp.composeapp.generated.resources.Res
import ecellkmp.composeapp.generated.resources.ic_user
import ecellkmp.composeapp.generated.resources.main_font
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProfileHeader(
    account: AccountModel,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(ColorAccent, ColorAccentLight)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Profile Image
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(ColorPrimaryLightest)
                    .border(4.dp, White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (account.profilePic.isNotEmpty()) {
                    // TODO: Load actual image
                    Image(
                        painter = painterResource(Res.drawable.ic_user),
                        contentDescription = "Profile picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(60.dp),
                        colorFilter = ColorFilter.tint(White)
                    )
                } else {
                    Image(
                        painter = painterResource(Res.drawable.ic_user),
                        contentDescription = "Profile picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(60.dp),
                        colorFilter = ColorFilter.tint(White)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Name
            Text(
                text = account.name,
                color = White,
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(Res.font.main_font)),
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(4.dp))

            // Designation
            if (account.designation.isNotEmpty()) {
                Text(
                    text = account.designation,
                    color = ColorAccentLightest,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(Res.font.main_font))
                )
            }

            Spacer(Modifier.height(8.dp))

            // Status Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        when (account.status) {
                            "VERIFIED" -> Color(0xFF14D678)
                            "IN REVIEW" -> Color(0xFFFFA500)
                            "PENDING" -> Color(0xFFFFD700)
                            "BLOCKED" -> ErrorRed
                            else -> ColorAccentLightest
                        }
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = account.status.ifEmpty { "UNKNOWN" },
                    color = if (account.status == "VERIFIED") Color(0xFF0B0A0A) else White,
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(Res.font.main_font)),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Edit Button
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(ColorPrimaryLightest)
                .clickable { onEditClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "✎",
                color = White,
                fontSize = 18.sp
            )
        }
    }
}

