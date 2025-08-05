package com.anantmittal.ecellkmp.utility.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = ColorPrimary,
    onPrimary = White,
    primaryContainer = ColorPrimaryLight, // Or another light variant
    onPrimaryContainer = White,
    secondary = ColorAccent, // Or your secondary color
    onSecondary = White,
    secondaryContainer = ColorAccentLight, // Or another light variant
    onSecondaryContainer = Black,
    tertiary = EndeavourLogoColor, // Example, choose an appropriate tertiary
    onTertiary = Black,
    tertiaryContainer = EndeavourNavColorTrans, // Example
    onTertiaryContainer = EndeavourNavColor,
    background = White,
    onBackground = Black,
    surface = ColorPrimaryLightest, // For cards, sheets, etc.
    onSurface = Black,
    surfaceVariant = ColorPrimaryLight, // Slightly different surface color
    onSurfaceVariant = Black,
    outline = ColorPrimaryLight, // For outlines like in OutlinedTextField
    inverseOnSurface = White, // For text that needs to contrast with surface when inverted
    inverseSurface = Black,   // For surfaces that need to contrast with surface when inverted
    inversePrimary = ColorAccentLight, // Inverse of primary, useful in specific cases
    error = ErrorRed,
    onError = OnErrorRed,
    errorContainer = ErrorContainerRed,
    onErrorContainer = OnErrorContainerRed,
    // scrim = Color.Black, // Usually default is fine
    // outlineVariant = Color.Gray // For less prominent outlines
)

private val DarkColorScheme = darkColorScheme(
    primary = ColorAccent, // Often a brighter color for dark theme primary
    onPrimary = Black,     // Text on dark theme primary
    primaryContainer = ColorAccentDark,
    onPrimaryContainer = White,
    secondary = EndeavourLogoColor, // Example for dark secondary
    onSecondary = Black,
    secondaryContainer = EndeavourBgColor, // Example
    onSecondaryContainer = White,
    tertiary = EndeavourLogoColor2, // Example
    onTertiary = Black,
    tertiaryContainer = EndeavourBgColor2,
    onTertiaryContainer = White,
    background = ColorPrimaryDark, // Dark background
    onBackground = White,          // Text on dark background
    surface = ColorPrimaryLight,   // Cards, sheets in dark theme (often a bit lighter than background)
    onSurface = White,             // Text on dark surface
    surfaceVariant = ColorPrimaryLightest, // Slightly different dark surface
    onSurfaceVariant = White,
    outline = ColorPrimaryLightest, // Outlines in dark theme
    inverseOnSurface = Black,
    inverseSurface = White,
    inversePrimary = ColorPrimaryLight,
    error = DarkErrorRed,
    onError = DarkOnErrorRed,
    errorContainer = DarkErrorContainerRed,
    onErrorContainer = DarkOnErrorContainerRed,
    // scrim = Color.Black, // Usually default is fine
    // outlineVariant = Color.LightGray // For less prominent outlines in dark
)

@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (useDarkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        // typography = Typography, // Assuming you have a Typography object defined
        // shapes = Shapes, // Assuming you have a Shapes object defined (optional)
        content = content
    )
}

// Placeholder for Typography - you should define this based on your app's needs
// e.g., in a Typography.kt file
//expect object Typography