package com.scorpio.distancecalculator.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.longPreferencesKey
import com.scorpio.distancecalculator.dataStore
import kotlinx.coroutines.flow.map

val LocalDualToneColors = staticCompositionLocalOf { ColorPresets.Tone_Option_2 }

@Composable
fun DualToneTheme(
    colors: DualTone,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val savedToneId by context.dataStore.data.map { it[longPreferencesKey("color_preset")] }
        .collectAsState(colors.toneId)
    val savedTone =
        remember(savedToneId) {
            ColorPresets.listOfTones.find { it.toneId == savedToneId } ?: colors
        }

    val colorScheme =
        ColorScheme(
            primary = savedTone.foreground,
            onPrimary = savedTone.background,
            primaryContainer = savedTone.foreground,
            onPrimaryContainer = savedTone.background,
            inversePrimary = savedTone.background,
            secondary = savedTone.foreground,
            onSecondary = savedTone.background,
            secondaryContainer = savedTone.foreground,
            onSecondaryContainer = savedTone.background,
            tertiary = savedTone.foreground,
            onTertiary = savedTone.background,
            tertiaryContainer = savedTone.foreground,
            onTertiaryContainer = savedTone.background,
            background = savedTone.background,
            onBackground = savedTone.foreground,
            surface = savedTone.background,
            onSurface = savedTone.foreground,
            surfaceVariant = savedTone.background,
            onSurfaceVariant = savedTone.foreground,
            surfaceTint = savedTone.foreground,
            inverseSurface = savedTone.foreground,
            inverseOnSurface = savedTone.background,
            error = savedTone.foreground,
            onError = savedTone.background,
            errorContainer = savedTone.foreground,
            onErrorContainer = savedTone.background,
            outline = savedTone.foreground,
            outlineVariant = savedTone.foreground,
            scrim = savedTone.background,
            surfaceBright = savedTone.background,
            surfaceDim = savedTone.background,
            surfaceContainer = savedTone.background,
            surfaceContainerHigh = savedTone.background,
            surfaceContainerHighest = savedTone.background,
            surfaceContainerLow = savedTone.background,
            surfaceContainerLowest = savedTone.background,
        )

    CompositionLocalProvider(LocalDualToneColors provides savedTone) {
        MaterialTheme(colorScheme = colorScheme, typography = Typography) {
            content()
        }
    }
}
