package com.anantmittal.ecellkmp.utility.presentation.animations

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.anantmittal.ecellkmp.utility.presentation.White
import ecellkmp.composeapp.generated.resources.Res
import ecellkmp.composeapp.generated.resources.ecell_e
import ecellkmp.composeapp.generated.resources.ecell_text
import ecellkmp.composeapp.generated.resources.leaguespartansemireg
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import kotlinx.coroutines.delay

@Composable
fun LoginTransitionAnimation(
    isTransitioned: Boolean,
    onTransitionComplete: () -> Unit = {},
    modifier: Modifier = Modifier,
    animationDurationMs: Int = 1200,
    content: @Composable (Boolean) -> Unit
) {
    val transition = updateTransition(targetState = isTransitioned, label = "login_transition")

    // Logo animations
    val logoScale by transition.animateFloat(
        transitionSpec = {
            tween(
                durationMillis = animationDurationMs,
                easing = FastOutSlowInEasing
            )
        },
        label = "logo_scale"
    ) { transitioned -> if (transitioned) 0.4f else 1f }

    val logoOffsetY by transition.animateDp(
        transitionSpec = {
            tween(
                durationMillis = animationDurationMs,
                easing = FastOutSlowInEasing
            )
        },
        label = "logo_offset_y"
    ) { transitioned -> if (transitioned) 280.dp else 0.dp }

    val logoOffsetX by transition.animateDp(
        transitionSpec = {
            tween(
                durationMillis = animationDurationMs,
                easing = FastOutSlowInEasing
            )
        },
        label = "logo_offset_x"
    ) { transitioned -> if (transitioned) (-60).dp else 0.dp }

    // Text animations
    val textScale by transition.animateFloat(
        transitionSpec = {
            tween(
                durationMillis = animationDurationMs,
                easing = FastOutSlowInEasing
            )
        },
        label = "text_scale"
    ) { transitioned -> if (transitioned) 0.4f else 1f }

    val textOffsetY by transition.animateDp(
        transitionSpec = {
            tween(
                durationMillis = animationDurationMs,
                easing = FastOutSlowInEasing
            )
        },
        label = "text_offset_y"
    ) { transitioned -> if (transitioned) 280.dp else 0.dp }

    val textOffsetX by transition.animateDp(
        transitionSpec = {
            tween(
                durationMillis = animationDurationMs,
                easing = FastOutSlowInEasing
            )
        },
        label = "text_offset_x"
    ) { transitioned -> if (transitioned) 40.dp else 0.dp }

    // Tagline fade animation
    val taglineAlpha by transition.animateFloat(
        transitionSpec = {
            tween(
                durationMillis = animationDurationMs / 2,
                easing = FastOutSlowInEasing
            )
        },
        label = "tagline_alpha"
    ) { transitioned -> if (transitioned) 0f else 1f }

    LaunchedEffect(isTransitioned) {
        if (isTransitioned) {
            delay(animationDurationMs.toLong())
            onTransitionComplete()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        content(isTransitioned)

        // Animated logo and text
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = logoScale
                    scaleY = logoScale
                    translationY = logoOffsetY.toPx()
                    translationX = logoOffsetX.toPx()
                },
            contentAlignment = if (isTransitioned) Alignment.BottomStart else Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.ecell_e),
                contentDescription = "ecell logo",
                modifier = Modifier.padding(
                    start = if (isTransitioned) 40.dp else 0.dp,
                    bottom = if (isTransitioned) 40.dp else 0.dp
                )
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = textScale
                    scaleY = textScale
                    translationY = textOffsetY.toPx()
                    translationX = textOffsetX.toPx()
                },
            contentAlignment = if (isTransitioned) Alignment.BottomStart else Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.ecell_text),
                contentDescription = "ecell text",
                modifier = Modifier.padding(
                    start = if (isTransitioned) 120.dp else 0.dp,
                    bottom = if (isTransitioned) 40.dp else 0.dp,
                    top = if (!isTransitioned) 60.dp else 0.dp
                )
            )
        }

        // Animated tagline
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = taglineAlpha },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "IDEATE CREATE INCUBATE",
                color = White,
                fontFamily = FontFamily(Font(Res.font.leaguespartansemireg)),
                modifier = Modifier.padding(top = 120.dp)
            )
        }
    }
}
