package com.anantmittal.ecellkmp.presentation.bottom_navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.anantmittal.ecellkmp.app.Route

data class BottomNavItemState(
    val label: String,
    val icon: ImageVector,
    val route: Route
)
