package com.anantmittal.ecellkmp.presentation.home_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.anantmittal.ecellkmp.utility.presentation.ColorAccent
import com.anantmittal.ecellkmp.utility.presentation.White
import ecellkmp.composeapp.generated.resources.Res
import ecellkmp.composeapp.generated.resources.ic_user
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProfileCard(
    modifier: Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .height(150.dp)
                .width(130.dp)
                .zIndex(1f)
                .offset(y = (50).dp),
            painter = painterResource(Res.drawable.ic_user),
            contentDescription = "profile image",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(White)
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12))
                .background(ColorAccent)
                .height(250.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(vertical = 70.dp, horizontal = 20.dp).fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("name")
                Text("designation")
            }
        }
    }
}