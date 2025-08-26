package com.anantmittal.ecellkmp.presentation.home_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.anantmittal.ecellkmp.utility.presentation.White
import ecellkmp.composeapp.generated.resources.Res
import ecellkmp.composeapp.generated.resources.image_not_found
import org.jetbrains.compose.resources.painterResource

@Composable
fun EventGlimpseBanner(
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12))
            .background(White)
            .fillMaxWidth()
            .height(180.dp)
    ) {
        Image(
            painter = painterResource(Res.drawable.image_not_found),
            contentDescription = "event glimpse image",
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(14.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text("name")
            Text("tag_line")
            Button(
                onClick = {},
                content = { Text("Explore now") },
            )
        }
    }
}