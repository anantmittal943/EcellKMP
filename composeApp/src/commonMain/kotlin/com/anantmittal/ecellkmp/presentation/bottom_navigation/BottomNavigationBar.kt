package com.anantmittal.ecellkmp.presentation.bottom_navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.anantmittal.ecellkmp.utility.presentation.ColorAccent
import com.anantmittal.ecellkmp.utility.presentation.White

@Composable
fun BottomNavigationBar(
    navController: NavController,
    items: List<BottomNavItemState>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    CompositionLocalProvider(
        LocalTextStyle provides LocalTextStyle.current.copy(
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        ),
        LocalContentColor provides White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(50),
                    ambientColor = ColorAccent,
                    spotColor = Color(
                        red = (ColorAccent.red * 3 + Color.Magenta.red) / 4f,
                        green = (ColorAccent.green * 3 + Color.Magenta.green) / 4f,
                        blue = (ColorAccent.blue * 3 + Color.Magenta.blue) / 4f,
                        alpha = (ColorAccent.alpha * 3 + Color.Magenta.alpha) / 4f
                    )
                )
                .clip(RoundedCornerShape(50))
                .background(
                    Color(
                        red = (ColorAccent.red * 3 + Color.Magenta.red) / 4f,
                        green = (ColorAccent.green * 3 + Color.Magenta.green) / 4f,
                        blue = (ColorAccent.blue * 3 + Color.Magenta.blue) / 4f,
                        alpha = (ColorAccent.alpha * 3 + Color.Magenta.alpha) / 4f
                    ).copy(alpha = 0.8f)
                )
                .padding(10.dp), // Inner padding for the items
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { itemState ->
                val selected = currentRoute == itemState.route.path

                val alpha by animateFloatAsState(
                    targetValue = if (selected) 1f else 0.5f,
                    label = "alpha"
                )

                val scale by animateFloatAsState(
                    targetValue = if (selected) 1.15f else 1f,
                    animationSpec = spring(
                        stiffness = Spring.StiffnessLow,
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                    ),
                    label = "scale"
                )

                Column(
                    modifier = Modifier
                        .scale(scale)
                        .alpha(alpha)
                        .fillMaxHeight()
                        .weight(1f)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            navController.navigate(itemState.route) {
                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(imageVector = itemState.icon, contentDescription = "tab ${itemState.label}")
                    Text(text = itemState.label)
                }
            }
        }
    }

//    NavigationBar(
//        containerColor = ColorAccent.copy(alpha = 0.9f),
//        modifier = Modifier
//            .height(IntrinsicSize.Min)
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 8.dp)
//            .shadow(
//                elevation = 8.dp,
//                shape = RoundedCornerShape(50)
//            )
//            .clip(RoundedCornerShape(50))
//    ) {
//        items.forEach { itemState ->
//            NavigationBarItem(
//                selected = currentRoute == itemState.route.path,
//                icon = { Icon(itemState.icon, itemState.label) },
//                label = { Text(itemState.label) },
//                onClick = {
//                    navController.navigate(itemState.route) {
//                        navController.graph.startDestinationRoute?.let { route ->
//                            popUpTo(route) {
//                                saveState = true
//                            }
//                        }
//                        launchSingleTop = true
//                        restoreState = true
//                    }
//                },
//                colors = NavigationBarItemDefaults.colors(
//                    selectedTextColor = White,
//                    unselectedTextColor = White.copy(alpha = 0.5f),
//                    indicatorColor = Color.Red.copy(alpha = 0.3f),
//                    selectedIconColor = White,
//                    unselectedIconColor = White.copy(alpha = 0.5f)
//                )
//            )
//        }
//    }
}