package com.anantmittal.ecellkmp.utility.presentation.animations

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

/*@Composable
@Preview
fun App() {
    var showSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(3000) // 3 seconds delay
        showSplash = false
    }

    MaterialTheme {
        ProfessionalScreenTransition(
            showFirst = showSplash,
            firstScreen = { SplashScreen() },
            secondScreen = { LoginSignupScreen() },
            animationDurationMs = 1000
        )
    }
}*/

@Composable
fun CrossFadeTransition(
    showFirst: Boolean,
    firstScreen: @Composable () -> Unit,
    secondScreen: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    animationDurationMs: Int = 600
) {
    Crossfade(
        targetState = showFirst,
        modifier = modifier,
        animationSpec = tween(
            durationMillis = animationDurationMs,
            easing = FastOutSlowInEasing
        )
    ) { isFirst ->
        if (isFirst) {
            firstScreen()
        } else {
            secondScreen()
        }
    }
}
