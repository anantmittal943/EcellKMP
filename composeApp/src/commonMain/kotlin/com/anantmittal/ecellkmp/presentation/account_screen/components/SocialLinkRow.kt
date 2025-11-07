package com.anantmittal.ecellkmp.presentation.account_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anantmittal.ecellkmp.utility.presentation.ColorAccent
import com.anantmittal.ecellkmp.utility.presentation.ColorAccentDark
import com.anantmittal.ecellkmp.utility.presentation.ColorAccentLightest
import ecellkmp.composeapp.generated.resources.Res
import ecellkmp.composeapp.generated.resources.main_font
import org.jetbrains.compose.resources.Font

@Composable
fun SocialLinkRow(
    label: String,
    onClick: () -> Unit,
    isLast: Boolean = false
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .clickable { onClick() }
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = ColorAccentLightest,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(Res.font.main_font))
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "View",
                    color = ColorAccent,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(Res.font.main_font)),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "→",
                    color = ColorAccent,
                    fontSize = 16.sp
                )
            }
        }

        if (!isLast) {
            HorizontalDivider(
                color = ColorAccentDark,
                thickness = 1.dp
            )
        }
    }
}

