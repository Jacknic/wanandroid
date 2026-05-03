package com.jacknic.android.wanandroid.ui.theme

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private fun darkColorSchemeFor(scheme: ThemeColorScheme) = darkColorScheme(
    primary = scheme.darkPrimary,
    secondary = scheme.darkSecondary,
    tertiary = scheme.darkTertiary
)

private fun lightColorSchemeFor(scheme: ThemeColorScheme) = lightColorScheme(
    primary = scheme.lightPrimary,
    secondary = scheme.lightSecondary,
    tertiary = scheme.lightTertiary
)

private fun darkColorSchemeForCustom(data: CustomColorData) = darkColorScheme(
    primary = data.primary,
    secondary = data.secondary,
    tertiary = data.tertiary
)

private fun lightColorSchemeForCustom(data: CustomColorData) = lightColorScheme(
    primary = data.primary,
    secondary = data.secondary,
    tertiary = data.tertiary
)

/**
 * 主题模式
 */
enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

var themeMode by mutableStateOf(ThemeMode.SYSTEM)

fun useThemeMode(mode: ThemeMode) {
    when (mode) {
        ThemeMode.SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        ThemeMode.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        ThemeMode.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}

var dynamicThemeColor by mutableStateOf(false)

var themeColorScheme by mutableStateOf(ThemeColorScheme.DEFAULT)

var customColorData by mutableStateOf(CustomColorData(Color(0xFF4483F4)))

fun useThemeColorScheme(scheme: ThemeColorScheme) {
    themeColorScheme = scheme
    // 选择预设主题颜色时自动关闭动态颜色
    if (dynamicThemeColor) {
        dynamicThemeColor = false
    }
}

fun useDynamicThemeColor(enabled: Boolean) {
    dynamicThemeColor = enabled
}

fun useCustomThemeColor(color: Color) {
    customColorData = CustomColorData(color)
    themeColorScheme = ThemeColorScheme.CUSTOM
    if (dynamicThemeColor) {
        dynamicThemeColor = false
    }
}

@Composable
fun WanandroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = dynamicThemeColor,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        themeColorScheme == ThemeColorScheme.CUSTOM -> {
            if (darkTheme) darkColorSchemeForCustom(customColorData)
            else lightColorSchemeForCustom(customColorData)
        }

        darkTheme -> darkColorSchemeFor(themeColorScheme)
        else -> lightColorSchemeFor(themeColorScheme)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
