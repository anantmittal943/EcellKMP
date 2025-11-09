package com.anantmittal.ecellkmp.utility.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.anantmittal.ecellkmp.utility.presentation.ColorAccent

/**
 * A centered loading indicator using Material 3 CircularProgressIndicator
 * Used across the app for consistent loading states
 */
@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = ColorAccent
        )
    }
}

