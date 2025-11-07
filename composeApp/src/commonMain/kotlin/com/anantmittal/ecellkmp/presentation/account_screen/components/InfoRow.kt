package com.anantmittal.ecellkmp.presentation.account_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anantmittal.ecellkmp.utility.presentation.ColorAccentDark
import com.anantmittal.ecellkmp.utility.presentation.ColorAccentLightest
import com.anantmittal.ecellkmp.utility.presentation.White
import ecellkmp.composeapp.generated.resources.Res
import ecellkmp.composeapp.generated.resources.main_font
import org.jetbrains.compose.resources.Font

@Composable
fun InfoRow(
    label: String,
    value: String,
    isLast: Boolean = false
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                color = ColorAccentLightest,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(Res.font.main_font))
            )

            Spacer(Modifier.width(16.dp))

            Text(
                text = value,
                color = White,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(Res.font.main_font)),
                fontWeight = FontWeight.Medium
            )
        }

        if (!isLast) {
            HorizontalDivider(
                color = ColorAccentDark,
                thickness = 1.dp
            )
        }
    }
}

