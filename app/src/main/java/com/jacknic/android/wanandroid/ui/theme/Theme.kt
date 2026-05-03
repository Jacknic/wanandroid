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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.jacknic.android.wanandroid.core.data.UserDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

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

var dynamicThemeColor by mutableStateOf(false)

var themeColorScheme by mutableStateOf(ThemeColorScheme.DEFAULT)

var customColorData by mutableStateOf(CustomColorData(Color(0xFF4483F4)))

/**
 * 设置持久化仓库引用
 */
private lateinit var repo: UserDataRepository

private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

/**
 * 初始化主题设置（从 DataStore 读取并恢复）
 * 应在 Application.onCreate 中调用
 */
suspend fun initThemeSettings(repository: UserDataRepository) {
    repo = repository
    val modeName = repository.themeModeFlow().first()
    val dynamic = repository.dynamicThemeColorFlow().first()
    val schemeName = repository.themeColorSchemeFlow().first()
    val customArgb = repository.customColorPrimaryFlow().first()

    themeMode = runCatching { ThemeMode.valueOf(modeName) }.getOrDefault(ThemeMode.SYSTEM)
    useThemeMode(themeMode)

    dynamicThemeColor = dynamic

    themeColorScheme =
        runCatching { ThemeColorScheme.valueOf(schemeName) }.getOrDefault(ThemeColorScheme.DEFAULT)

    customColorData = CustomColorData(Color(customArgb))
}

fun useThemeMode(mode: ThemeMode) {
    themeMode = mode
    repo.let { ioScope.launch { it.setThemeMode(mode.name) } }
    when (mode) {
        ThemeMode.SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        ThemeMode.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        ThemeMode.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}

fun useThemeColorScheme(scheme: ThemeColorScheme) {
    themeColorScheme = scheme
    repo.let { ioScope.launch { it.setThemeColorScheme(scheme.name) } }
    // 选择预设主题颜色时自动关闭动态颜色
    if (dynamicThemeColor) {
        dynamicThemeColor = false
        repo.let { ioScope.launch { it.setDynamicThemeColor(false) } }
    }
}

fun useDynamicThemeColor(enabled: Boolean) {
    dynamicThemeColor = enabled
    repo.let { ioScope.launch { it.setDynamicThemeColor(enabled) } }
}

fun useCustomThemeColor(color: Color) {
    customColorData = CustomColorData(color)
    themeColorScheme = ThemeColorScheme.CUSTOM
    repo.let {
        ioScope.launch {
            it.setCustomColorPrimary(color.toArgb())
            it.setThemeColorScheme(ThemeColorScheme.CUSTOM.name)
        }
    }
    if (dynamicThemeColor) {
        dynamicThemeColor = false
        repo.let { ioScope.launch { it.setDynamicThemeColor(false) } }
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
