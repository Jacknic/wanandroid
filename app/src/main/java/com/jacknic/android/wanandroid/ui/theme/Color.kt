package com.jacknic.android.wanandroid.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

// === 默认蓝 ===
private val DefaultBlueDarkPrimary = Color(0xFF4483F4)
private val DefaultBlueDarkSecondary = Color(0xFF3FABFF)
private val DefaultBlueDarkTertiary = Color(0xFFFC7E75)
private val DefaultBlueLightPrimary = Color(0xFF4483F4)
private val DefaultBlueLightSecondary = Color(0xFF3FABFF)
private val DefaultBlueLightTertiary = Color(0xFF7E403C)

// === 翠绿 ===
private val GreenDarkPrimary = Color(0xFF4CAF50)
private val GreenDarkSecondary = Color(0xFF81C784)
private val GreenDarkTertiary = Color(0xFFA5D6A7)
private val GreenLightPrimary = Color(0xFF388E3C)
private val GreenLightSecondary = Color(0xFF66BB6A)
private val GreenLightTertiary = Color(0xFF2E7D32)

// === 紫色 ===
private val PurpleDarkPrimary = Color(0xFFBB86FC)
private val PurpleDarkSecondary = Color(0xFF985EFF)
private val PurpleDarkTertiary = Color(0xFFCF9DFF)
private val PurpleLightPrimary = Color(0xFF6200EE)
private val PurpleLightSecondary = Color(0xFF7C4DFF)
private val PurpleLightTertiary = Color(0xFF3700B3)

// === 橙色 ===
private val OrangeDarkPrimary = Color(0xFFFF9800)
private val OrangeDarkSecondary = Color(0xFFFFB74D)
private val OrangeDarkTertiary = Color(0xFFFFCC80)
private val OrangeLightPrimary = Color(0xFFF57C00)
private val OrangeLightSecondary = Color(0xFFFF9800)
private val OrangeLightTertiary = Color(0xFFE65100)

// === 红色 ===
private val RedDarkPrimary = Color(0xFFEF5350)
private val RedDarkSecondary = Color(0xFFE57373)
private val RedDarkTertiary = Color(0xFFEF9A9A)
private val RedLightPrimary = Color(0xFFD32F2F)
private val RedLightSecondary = Color(0xFFF44336)
private val RedLightTertiary = Color(0xFFC62828)

/**
 * 将 Compose Color 转为 Android HSV 数组
 */
private fun Color.toHsv(): FloatArray {
    val hsv = FloatArray(3)
    android.graphics.Color.colorToHSV(this.toArgb(), hsv)
    return hsv
}

/**
 * 从 HSV 创建 Compose Color
 */
private fun hsvToColor(hsv: FloatArray): Color = Color(android.graphics.Color.HSVToColor(hsv))

/**
 * 根据主色生成辅助色（降低饱和度、微调亮度）
 */
private fun Color.deriveSecondary(): Color {
    val hsv = toHsv()
    hsv[1] = (hsv[1] * 0.6f).coerceIn(0f, 1f)
    hsv[2] = (hsv[2] + 0.1f).coerceIn(0f, 1f)
    return hsvToColor(hsv)
}

/**
 * 根据主色生成第三色（色相偏移、降低饱和度）
 */
private fun Color.deriveTertiary(): Color {
    val hsv = toHsv()
    hsv[0] = (hsv[0] + 30f) % 360f
    hsv[1] = (hsv[1] * 0.5f).coerceIn(0f, 1f)
    return hsvToColor(hsv)
}

/**
 * 自定义颜色方案数据
 */
data class CustomColorData(
    val primary: Color,
    val secondary: Color = primary.deriveSecondary(),
    val tertiary: Color = primary.deriveTertiary(),
)

/**
 * 主题颜色方案
 */
enum class ThemeColorScheme(
    val label: String,
    val lightPrimary: Color,
    val lightSecondary: Color,
    val lightTertiary: Color,
    val darkPrimary: Color,
    val darkSecondary: Color,
    val darkTertiary: Color,
    val swatchColor: Color = lightPrimary,
    val isCustom: Boolean = false,
) {
    DEFAULT(
        label = "默认蓝",
        lightPrimary = DefaultBlueLightPrimary,
        lightSecondary = DefaultBlueLightSecondary,
        lightTertiary = DefaultBlueLightTertiary,
        darkPrimary = DefaultBlueDarkPrimary,
        darkSecondary = DefaultBlueDarkSecondary,
        darkTertiary = DefaultBlueDarkTertiary,
    ),
    GREEN(
        label = "翠绿",
        lightPrimary = GreenLightPrimary,
        lightSecondary = GreenLightSecondary,
        lightTertiary = GreenLightTertiary,
        darkPrimary = GreenDarkPrimary,
        darkSecondary = GreenDarkSecondary,
        darkTertiary = GreenDarkTertiary,
    ),
    PURPLE(
        label = "紫色",
        lightPrimary = PurpleLightPrimary,
        lightSecondary = PurpleLightSecondary,
        lightTertiary = PurpleLightTertiary,
        darkPrimary = PurpleDarkPrimary,
        darkSecondary = PurpleDarkSecondary,
        darkTertiary = PurpleDarkTertiary,
    ),
    ORANGE(
        label = "橙色",
        lightPrimary = OrangeLightPrimary,
        lightSecondary = OrangeLightSecondary,
        lightTertiary = OrangeLightTertiary,
        darkPrimary = OrangeDarkPrimary,
        darkSecondary = OrangeDarkSecondary,
        darkTertiary = OrangeDarkTertiary,
    ),
    RED(
        label = "红色",
        lightPrimary = RedLightPrimary,
        lightSecondary = RedLightSecondary,
        lightTertiary = RedLightTertiary,
        darkPrimary = RedDarkPrimary,
        darkSecondary = RedDarkSecondary,
        darkTertiary = RedDarkTertiary,
    ),
    CUSTOM(
        label = "自定义",
        lightPrimary = Color.Magenta,
        lightSecondary = Color.Magenta,
        lightTertiary = Color.Magenta,
        darkPrimary = Color.Magenta,
        darkSecondary = Color.Magenta,
        darkTertiary = Color.Magenta,
        isCustom = true,
    ),
}
