package com.jacknic.android.wanandroid.ui.page.setting

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Check
import androidx.compose.material.icons.twotone.CheckCircle
import androidx.compose.material.icons.twotone.Face
import androidx.compose.material.icons.twotone.KeyboardArrowRight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jacknic.android.wanandroid.R
import com.jacknic.android.wanandroid.ui.page.LocalNavCtrl
import com.jacknic.android.wanandroid.ui.theme.ThemeMode
import com.jacknic.android.wanandroid.ui.theme.dynamicThemeColor
import com.jacknic.android.wanandroid.ui.theme.themeMode

/**
 * 应用设置页
 *
 * @author Jacknic
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageSetting() {
    val nav = LocalNavCtrl.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(topBar = {
        TopAppBar(title = { Text("设置") }, navigationIcon = {
            IconButton(onClick = { nav.navigateUp() }) {
                Icon(Icons.TwoTone.ArrowBack, "")
            }
        }, scrollBehavior = scrollBehavior)
    }) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(1f)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            item {
                Text(
                    "常规",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp, 0.dp)
                )
            }
            item {
                val themeModeNames = stringArrayResource(R.array.theme_mode_names)
                val showModePanel = rememberSaveable { mutableStateOf(false) }
                val interactionSource = remember { MutableInteractionSource() }
                ListItem(leadingContent = { Icon(Icons.TwoTone.Face, "") },
                    headlineText = { Text(stringResource(R.string.title_theme_mode)) },
                    supportingText = {
                        ThemeModePanel(showModePanel, themeModeNames)
                        Text(themeModeNames[themeMode.ordinal])
                    },
                    trailingContent = {

                        Icon(Icons.TwoTone.KeyboardArrowRight, "")
                    },
                    modifier = Modifier.clickable(interactionSource = interactionSource, null) {
                        showModePanel.value = true
                    })
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                item {
                    val interactionSource = remember { MutableInteractionSource() }
                    ListItem(leadingContent = { Icon(Icons.TwoTone.CheckCircle, "") },
                        headlineText = { Text(stringResource(R.string.title_dynamic_theme_color)) },
                        supportingText = { Text(stringResource(R.string.desc_dynamic_theme_color)) },
                        trailingContent = {
                            Switch(dynamicThemeColor, onCheckedChange = {
                                dynamicThemeColor = !dynamicThemeColor
                            }, interactionSource = interactionSource)
                        },
                        modifier = Modifier.clickable(interactionSource = interactionSource, null) {
                            dynamicThemeColor = !dynamicThemeColor
                        })
                }
            }
            for (i in 0..20) {
                item {
                    var checked by remember {
                        mutableStateOf(
                            if (i == 0) dynamicThemeColor else false
                        )
                    }
                    val interactionSource = remember { MutableInteractionSource() }
                    ListItem(leadingContent = { Icon(Icons.TwoTone.Face, "") },
                        headlineText = { Text("主题模式") },
                        supportingText = { Text("系统默认") },
                        trailingContent = {
                            Switch(checked, onCheckedChange = {
                                checked = !checked
                            }, interactionSource = interactionSource)
                        },
                        modifier = Modifier.clickable(interactionSource = interactionSource, null) {
                            checked = !checked
                        }

                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ThemeModePanel(showModePanel: MutableState<Boolean>, themeModeNames: Array<String>) {
    DropdownMenu(
        expanded = showModePanel.value,
        onDismissRequest = { showModePanel.value = false }
    ) {
        val colorPrimary = MaterialTheme.colorScheme.primary
        ThemeMode.values().forEachIndexed { index, mode ->
            val modeName = themeModeNames[index]
            val selected = themeMode == mode
            ListItem(
                headlineText = {
                    Text(modeName, color = if (selected) colorPrimary else Color.Unspecified)
                },
                trailingContent = {
                    if (selected) {
                        Icon(Icons.TwoTone.Check, "", tint = colorPrimary)
                    }
                },
                modifier = Modifier.clickable {
                    themeMode = mode
                    showModePanel.value = false
                }
            )
        }
    }
}
