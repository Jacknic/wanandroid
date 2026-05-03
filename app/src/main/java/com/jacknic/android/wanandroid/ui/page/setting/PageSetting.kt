package com.jacknic.android.wanandroid.ui.page.setting

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material.icons.automirrored.twotone.KeyboardArrowRight
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.Check
import androidx.compose.material.icons.twotone.DarkMode
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.Language
import androidx.compose.material.icons.twotone.Notifications
import androidx.compose.material.icons.twotone.Palette
import androidx.compose.material.icons.twotone.Person
import androidx.compose.material.icons.twotone.Update
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jacknic.android.wanandroid.BuildConfig
import com.jacknic.android.wanandroid.R
import com.jacknic.android.wanandroid.ui.page.LocalNavCtrl
import com.jacknic.android.wanandroid.ui.page.Page
import com.jacknic.android.wanandroid.ui.page.login.LoginViewModel
import com.jacknic.android.wanandroid.ui.page.navTop
import com.jacknic.android.wanandroid.ui.theme.ThemeColorScheme
import com.jacknic.android.wanandroid.ui.theme.ThemeMode
import com.jacknic.android.wanandroid.ui.theme.customColorData
import com.jacknic.android.wanandroid.ui.theme.dynamicThemeColor
import com.jacknic.android.wanandroid.ui.theme.themeColorScheme
import com.jacknic.android.wanandroid.ui.theme.themeMode
import com.jacknic.android.wanandroid.ui.theme.useCustomThemeColor
import com.jacknic.android.wanandroid.ui.theme.useDynamicThemeColor
import com.jacknic.android.wanandroid.ui.theme.useThemeColorScheme
import com.jacknic.android.wanandroid.ui.theme.useThemeMode

/**
 * 应用设置页
 *
 * @author Jacknic
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PageSetting(vm: LoginViewModel = hiltViewModel()) {
    val nav = LocalNavCtrl.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var showLogoutDialog by rememberSaveable { mutableStateOf(false) }
    Scaffold(topBar = {
        TopAppBar(title = { Text("设置") }, navigationIcon = {
            IconButton(onClick = { nav.navigateUp() }) {
                Icon(Icons.AutoMirrored.TwoTone.ArrowBack, "")
            }
        }, scrollBehavior = scrollBehavior)
    }) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier
                .fillMaxSize(1f)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            // === 账号管理 ===
            item {
                SectionHeader(title = "账号管理")
            }
            item {
                val interactionSource = remember { MutableInteractionSource() }
                ListItem(
                    leadingContent = {
                        Icon(Icons.TwoTone.Person, "", modifier = Modifier.size(24.dp))
                    },
                    headlineContent = { Text("编辑资料") },
                    trailingContent = {
                        Icon(Icons.AutoMirrored.TwoTone.KeyboardArrowRight, "")
                    },
                    modifier = Modifier.clickable(interactionSource = interactionSource, null) {
                        // TODO: 跳转编辑资料页
                    }
                )
            }

            // === 通用设置 ===
            item {
                SectionHeader(title = "通用设置")
            }
            item {
                val themeModeNames = stringArrayResource(R.array.theme_mode_names)
                val showModePanel = rememberSaveable { mutableStateOf(false) }
                val interactionSource = remember { MutableInteractionSource() }
                ListItem(
                    leadingContent = {
                        Icon(Icons.TwoTone.DarkMode, "", modifier = Modifier.size(24.dp))
                    },
                    headlineContent = { Text(stringResource(R.string.title_theme_mode)) },
                    supportingContent = {
                        ThemeModePanel(showModePanel, themeModeNames)
                        Text(themeModeNames[themeMode.ordinal])
                    },
                    trailingContent = {
                        Icon(Icons.AutoMirrored.TwoTone.KeyboardArrowRight, "")
                    },
                    modifier = Modifier.clickable(interactionSource = interactionSource, null) {
                        showModePanel.value = true
                    })
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                item {
                    val interactionSource = remember { MutableInteractionSource() }
                    ListItem(
                        leadingContent = {
                            Icon(Icons.TwoTone.Palette, "", modifier = Modifier.size(24.dp))
                        },
                        headlineContent = { Text(stringResource(R.string.title_dynamic_theme_color)) },
                        supportingContent = { Text(stringResource(R.string.desc_dynamic_theme_color)) },
                        trailingContent = {
                            Switch(
                                dynamicThemeColor,
                                onCheckedChange = { useDynamicThemeColor(it) },
                                interactionSource = interactionSource
                            )
                        },
                        modifier = Modifier.clickable(interactionSource = interactionSource, null) {
                            useDynamicThemeColor(!dynamicThemeColor)
                        })
                }
            }
            if (!dynamicThemeColor) {
                item {
                    ThemeColorSelector()
                }
            }
            item {
                val languageNames = stringArrayResource(R.array.language_names)
                val languageCodes = stringArrayResource(R.array.language_codes)
                val showLanguagePanel = rememberSaveable { mutableStateOf(false) }
                val currentLocale = AppCompatDelegate.getApplicationLocales()
                val currentIndex = remember(currentLocale) {
                    if (currentLocale.isEmpty) 0
                    else languageCodes.indexOfFirst { it.isNotEmpty() && it == currentLocale.get(0)?.language }
                        .coerceAtLeast(0)
                }
                val interactionSource = remember { MutableInteractionSource() }
                ListItem(
                    leadingContent = {
                        Icon(Icons.TwoTone.Language, "", modifier = Modifier.size(24.dp))
                    },
                    headlineContent = { Text("语言设置") },
                    supportingContent = {
                        LanguagePanel(showLanguagePanel, languageNames, languageCodes)
                        Text(languageNames[currentIndex])
                    },
                    trailingContent = {
                        Icon(Icons.AutoMirrored.TwoTone.KeyboardArrowRight, "")
                    },
                    modifier = Modifier.clickable(interactionSource = interactionSource, null) {
                        showLanguagePanel.value = true
                    }
                )
            }
            item {
                val interactionSource = remember { MutableInteractionSource() }
                ListItem(
                    leadingContent = {
                        Icon(Icons.TwoTone.Notifications, "", modifier = Modifier.size(24.dp))
                    },
                    headlineContent = { Text("推送通知") },
                    trailingContent = {
                        Icon(Icons.AutoMirrored.TwoTone.KeyboardArrowRight, "")
                    },
                    modifier = Modifier.clickable(interactionSource = interactionSource, null) {
                        // TODO: 跳转推送通知设置
                    }
                )
            }

            // === 其他 ===
            item {
                SectionHeader(title = "其他")
            }
            item {
                val interactionSource = remember { MutableInteractionSource() }
                ListItem(
                    leadingContent = {
                        Icon(Icons.TwoTone.Update, "", modifier = Modifier.size(24.dp))
                    },
                    headlineContent = { Text("检查更新") },
                    supportingContent = { Text("当前版本: v${BuildConfig.VERSION_NAME}") },
                    trailingContent = {
                        Icon(Icons.AutoMirrored.TwoTone.KeyboardArrowRight, "")
                    },
                    modifier = Modifier.clickable(interactionSource = interactionSource, null) {
                        // TODO: 检查更新
                    }
                )
            }
            item {
                val interactionSource = remember { MutableInteractionSource() }
                ListItem(
                    leadingContent = {
                        Icon(Icons.TwoTone.Info, "", modifier = Modifier.size(24.dp))
                    },
                    headlineContent = { Text("关于") },
                    trailingContent = {
                        Icon(Icons.AutoMirrored.TwoTone.KeyboardArrowRight, "")
                    },
                    modifier = Modifier.clickable(interactionSource = interactionSource, null) {
                        // TODO: 跳转关于页
                    }
                )
            }

            // === 退出登录 ===
            item {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            showLogoutDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier
                            .height(50.dp)
                            .widthIn(max = 360.dp)
                    ) {
                        Text(
                            text = "退出登录",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // === 版本号 ===
            item {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "当前版本: v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text(text = "确定要退出登录？") },
                text = {
                    Text("退出后将需要重新登录您的账号")
                },
                confirmButton = {
                    TextButton(onClick = {
                        showLogoutDialog = false
                        vm.logout()
                        nav.navTop(Page.Login, Page.Main)
                    }) { Text("确定") }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) { Text("取消") }
                },
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        title,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 4.dp)
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ThemeColorSelector() {
    var showColorPicker by rememberSaveable { mutableStateOf(false) }
    val isDynamicActive = dynamicThemeColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    ListItem(
        leadingContent = {
            Icon(Icons.TwoTone.Palette, "", modifier = Modifier.size(24.dp))
        },
        headlineContent = { Text("主题颜色") },
        supportingContent = {
            if (isDynamicActive) {
                Text(
                    "动态颜色已开启",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            ) {
                ThemeColorScheme.entries.forEach { scheme ->
                    val selected = themeColorScheme == scheme && !isDynamicActive
                    val swatchColor = if (scheme == ThemeColorScheme.CUSTOM) {
                        customColorData.primary
                    } else {
                        scheme.swatchColor
                    }
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(swatchColor)
                            .then(
                                if (selected) {
                                    Modifier.border(
                                        3.dp,
                                        MaterialTheme.colorScheme.primary,
                                        CircleShape
                                    )
                                } else Modifier
                            )
                            .then(
                                if (isDynamicActive && scheme != ThemeColorScheme.CUSTOM) Modifier
                                else Modifier.clickable {
                                    if (scheme == ThemeColorScheme.CUSTOM) {
                                        showColorPicker = true
                                    } else {
                                        useThemeColorScheme(scheme)
                                    }
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (scheme == ThemeColorScheme.CUSTOM && !selected) {
                            Icon(
                                Icons.TwoTone.Add,
                                contentDescription = "自定义颜色",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        } else if (selected) {
                            Icon(
                                Icons.TwoTone.Check,
                                contentDescription = scheme.label,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    )
    if (showColorPicker) {
        ColorPickerDialog(
            initialColor = customColorData.primary,
            onDismiss = { showColorPicker = false },
            onConfirm = { color ->
                useCustomThemeColor(color)
                showColorPicker = false
            }
        )
    }
}

@Composable
private fun ColorPickerDialog(
    initialColor: Color,
    onDismiss: () -> Unit,
    onConfirm: (Color) -> Unit
) {
    var hue by remember { mutableFloatStateOf(0f) }
    var saturation by remember { mutableFloatStateOf(1f) }
    var lightness by remember { mutableFloatStateOf(0.5f) }
    var hexText by remember { mutableStateOf(TextFieldValue("000000")) }

    // 从 initialColor 初始化 HSV 和 RGB
    LaunchedEffect(initialColor) {
        val hsv = FloatArray(3)
        android.graphics.Color.colorToHSV(initialColor.toArgb(), hsv)
        hue = hsv[0]
        saturation = hsv[1]
        lightness = hsv[2]
        hexText = TextFieldValue(argbToHex(initialColor.toArgb()))
    }

    val selectedColor = remember(hue, saturation, lightness) {
        Color(android.graphics.Color.HSVToColor(floatArrayOf(hue, saturation, lightness)))
    }

    fun onHexChange() {
        val rgb = hexToArgb(hexText.text) ?: return
        val hsv = FloatArray(3)
        android.graphics.Color.colorToHSV(rgb, hsv)
        hue = hsv[0]
        saturation = hsv[1]
        lightness = hsv[2]
    }

    // HSV 变化时同步十六进制文本
    LaunchedEffect(hue, saturation, lightness) {
        hexText = TextFieldValue(argbToHex(selectedColor.toArgb()))
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("选择自定义颜色") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 预览色块
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(selectedColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "预览",
                        color = if (lightness > 0.5f) Color.Black else Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                // 十六进制 RGB 输入
                Column {
                    Text("十六进制", style = MaterialTheme.typography.bodySmall)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = hexText,
                            onValueChange = { hexText = it; onHexChange() },
                            label = { Text("RRGGBB") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            prefix = { Text("#") }
                        )
                    }
                }
                // 色相
                Column {
                    Text("色相", style = MaterialTheme.typography.bodySmall)
                    Slider(
                        value = hue,
                        onValueChange = { hue = it },
                        valueRange = 0f..360f
                    )
                }
                // 饱和度
                Column {
                    Text("饱和度", style = MaterialTheme.typography.bodySmall)
                    Slider(
                        value = saturation,
                        onValueChange = { saturation = it },
                        valueRange = 0f..1f
                    )
                }
                // 亮度
                Column {
                    Text("亮度", style = MaterialTheme.typography.bodySmall)
                    Slider(
                        value = lightness,
                        onValueChange = { lightness = it },
                        valueRange = 0f..1f
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selectedColor) }) { Text("确定") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ThemeModePanel(showModePanel: MutableState<Boolean>, themeModeNames: Array<String>) {
    DropdownMenu(
        expanded = showModePanel.value,
        onDismissRequest = { showModePanel.value = false }
    ) {
        val colorPrimary = MaterialTheme.colorScheme.primary
        ThemeMode.entries.forEachIndexed { index, mode ->
            val modeName = themeModeNames[index]
            val selected = themeMode == mode
            ListItem(
                headlineContent = {
                    Text(modeName, color = if (selected) colorPrimary else Color.Unspecified)
                },
                trailingContent = {
                    if (selected) {
                        Icon(Icons.TwoTone.Check, "", tint = colorPrimary)
                    }
                },
                modifier = Modifier.clickable {
                    themeMode = mode
                    useThemeMode(mode)
                    showModePanel.value = false
                }
            )
        }
    }
}

private fun argbToHex(argb: Int): String {
    val r = (argb shr 16) and 0xFF
    val g = (argb shr 8) and 0xFF
    val b = argb and 0xFF
    return "%02X%02X%02X".format(r, g, b)
}

private fun hexToArgb(hex: String): Int? {
    val clean = hex.removePrefix("#").trim()
    if (clean.length != 6) return null
    return try {
        val r = clean.substring(0, 2).toInt(16)
        val g = clean.substring(2, 4).toInt(16)
        val b = clean.substring(4, 6).toInt(16)
        (0xFF shl 24) or (r shl 16) or (g shl 8) or b
    } catch (_: NumberFormatException) {
        null
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun LanguagePanel(
    showLanguagePanel: MutableState<Boolean>,
    languageNames: Array<String>,
    languageCodes: Array<String>
) {
    DropdownMenu(
        expanded = showLanguagePanel.value,
        onDismissRequest = { showLanguagePanel.value = false }
    ) {
        val colorPrimary = MaterialTheme.colorScheme.primary
        val currentLocale = AppCompatDelegate.getApplicationLocales()
        languageNames.forEachIndexed { index, name ->
            val selected = if (index == 0) currentLocale.isEmpty
            else currentLocale.get(0)?.language == languageCodes[index]
            ListItem(
                headlineContent = {
                    Text(name, color = if (selected) colorPrimary else Color.Unspecified)
                },
                trailingContent = {
                    if (selected) {
                        Icon(Icons.TwoTone.Check, "", tint = colorPrimary)
                    }
                },
                modifier = Modifier.clickable {
                    val code = languageCodes[index]
                    if (code.isEmpty()) {
                        AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
                    } else {
                        AppCompatDelegate.setApplicationLocales(
                            LocaleListCompat.forLanguageTags(
                                code
                            )
                        )
                    }
                    showLanguagePanel.value = false
                }
            )
        }
    }
}
