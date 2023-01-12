package com.github.bkmbigo.visiozoezi.common.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val lightColorScheme = lightColorScheme(
    primary = primaryColor,
    secondary = secondaryColor,
    primaryContainer = primaryContainerColor,
    background = backgroundColor,
    surface = surfaceColor,
    onPrimary = textColor
)

private val darkColorScheme = darkColorScheme(
    primary = darkPrimaryColor,
    secondary = darkSecondaryColor,
    primaryContainer = darkPrimaryContainerColor,
    background = darkBackgroundColor,
    surface = darkSurfaceColor,
    onPrimary = darkTextColor
)

@Composable
fun VisioZoeziTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if(darkTheme) darkColorScheme else lightColorScheme,
        content = content
    )
}