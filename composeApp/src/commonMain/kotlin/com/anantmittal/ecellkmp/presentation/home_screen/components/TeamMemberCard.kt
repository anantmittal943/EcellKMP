package com.anantmittal.ecellkmp.presentation.home_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anantmittal.ecellkmp.domain.models.AccountModel
import com.anantmittal.ecellkmp.utility.presentation.ColorAccentLight
import com.anantmittal.ecellkmp.utility.presentation.ColorAccentLightest
import com.anantmittal.ecellkmp.utility.presentation.Transparent30
import com.anantmittal.ecellkmp.utility.presentation.White
import ecellkmp.composeapp.generated.resources.Res
import ecellkmp.composeapp.generated.resources.ic_user
import ecellkmp.composeapp.generated.resources.main_font
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource

@Composable
fun TeamMemberCard(
    accountModel: AccountModel,
    onClick: (AccountModel) -> Unit,
    modifier: Modifier = Modifier
) {
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(ColorAccentLight)
            .clickable { onClick(accountModel) }
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(30.dp),
                color = White
            )
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Profile Image Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(ColorAccentLight)
            ) {
                if (accountModel.profilePic.isNotEmpty()) {
                    // TODO: Load image from URL using Coil or similar
                    // For now, show placeholder
                    Image(
                        painter = painterResource(Res.drawable.ic_user),
                        contentDescription = "Profile image of ${accountModel.name}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        colorFilter = ColorFilter.tint(White)
                    )
                } else {
                    Image(
                        painter = painterResource(Res.drawable.ic_user),
                        contentDescription = "Profile image of ${accountModel.name}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        colorFilter = ColorFilter.tint(White)
                    )
                }
            }

            // Name and Position Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Transparent30)
                    .padding(start = 6.dp, top = 4.dp, end = 6.dp, bottom = 4.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = accountModel.name.ifEmpty { "Member" },
                    color = White,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(Res.font.main_font)),
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "~ ${accountModel.designation.ifEmpty { "Position" }}",
                    color = ColorAccentLightest,
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(Res.font.main_font)),
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

