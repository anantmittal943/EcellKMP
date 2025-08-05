package com.anantmittal.ecellkmp.presentation.login_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ecellkmp.composeapp.generated.resources.Res
import ecellkmp.composeapp.generated.resources.ecell_e
import ecellkmp.composeapp.generated.resources.ecell_text
import org.jetbrains.compose.resources.painterResource

@Composable
fun LoginEcellLogo(
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .height(50.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.ecell_e),
            contentDescription = "ecell logo"
        )
        Spacer(modifier = modifier.width(6.dp))
        Image(
            painter = painterResource(Res.drawable.ecell_text),
            contentDescription = "ecell logo"
        )
    }
}