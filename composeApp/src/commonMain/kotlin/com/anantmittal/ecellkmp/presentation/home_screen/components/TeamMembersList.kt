package com.anantmittal.ecellkmp.presentation.home_screen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anantmittal.ecellkmp.domain.models.AccountModel
import com.anantmittal.ecellkmp.utility.presentation.ColorAccentLight
import com.anantmittal.ecellkmp.utility.presentation.ColorPrimaryLightest
import com.anantmittal.ecellkmp.utility.presentation.White
import ecellkmp.composeapp.generated.resources.Res
import ecellkmp.composeapp.generated.resources.main_font
import org.jetbrains.compose.resources.Font

@Composable
fun TeamMembersList(
    teamMembers: List<AccountModel>,
    onTeamMemberClick: (AccountModel) -> Unit,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier,
    showViewAllButton: Boolean = true,
    itemsToShow: Int? = null
) {
    val itemsToDisplay = if (itemsToShow != null) {
        teamMembers.take(itemsToShow)
    } else {
        teamMembers
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Use regular Column with Row instead of LazyGrid
        // This works better inside scrollable parent
        val chunked = itemsToDisplay.chunked(2)

        chunked.forEach { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 3.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                rowItems.forEach { teamMember ->
                    TeamMemberCard(
                        accountModel = teamMember,
                        onClick = onTeamMemberClick,
                        modifier = Modifier
                            .weight(1f)
                            .padding(3.dp)
                    )
                }
                // Add empty space if odd number of items in last row
                if (rowItems.size < 2) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }

        if (showViewAllButton && itemsToShow != null && teamMembers.size > itemsToShow) {
            Button(
                onClick = onViewAllClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 10.dp, end = 20.dp, bottom = 10.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorPrimaryLightest
                ),
                border = BorderStroke(2.dp, ColorAccentLight)
            ) {
                Text(
                    text = "View All",
                    color = White,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(Res.font.main_font))
                )
            }
        }
    }
}

