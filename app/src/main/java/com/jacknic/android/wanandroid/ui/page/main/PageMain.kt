package com.jacknic.android.wanandroid.ui.page.main

import android.annotation.SuppressLint
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.jacknic.android.wanandroid.R
import com.jacknic.android.wanandroid.ui.page.main.category.PageCategory
import com.jacknic.android.wanandroid.ui.page.main.discovery.PageDiscovery
import com.jacknic.android.wanandroid.ui.page.main.home.PageHome
import com.jacknic.android.wanandroid.ui.page.main.mine.PageMine
import com.jacknic.android.wanandroid.ui.page.main.tree.PageTree
import com.jacknic.android.wanandroid.core.ui.R as UR

enum class NavDestinations(
    @StringRes val label: Int,
    @RawRes val icon: Int,
    @StringRes val contentDescription: Int,
) {
    HOME(R.string.title_home, UR.raw.tabbar_animate_home, R.string.title_home),
    CATEGORY(R.string.title_category, UR.raw.tabbar_animate_course, R.string.title_category),
    DISCOVERY(R.string.title_discovery, UR.raw.tabbar_animate_discover, R.string.title_discovery),
    SYSTEM(R.string.title_system, UR.raw.tabbar_animate_dynamic, R.string.title_system),
    MINE(R.string.title_mine, UR.raw.tabbar_animate_mine, R.string.title_mine),
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PageMain() {
    var currentDestination by rememberSaveable { mutableStateOf(NavDestinations.HOME) }
    val suiteItemColors = NavigationSuiteDefaults.itemColors()
    val navSuiteType =
        NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo())
    NavigationSuiteScaffold(
        layoutType = navSuiteType,
        navigationSuiteItems = {
            NavDestinations.entries.forEach {
                val selected = it == currentDestination
                item(
                    icon = {
                        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(it.icon))
                        val progress by animateLottieCompositionAsState(
                            composition,
                            speed = if (selected) 1f else -3f,
                        )
                        LottieAnimation(
                            modifier = Modifier.size(24.dp),
                            composition = composition,
                            progress = { progress }
                        )
                    },
                    label = {
                        val textColor = if (selected) MaterialTheme.colorScheme.primary
                        else Color.Unspecified
                        val colorState by animateColorAsState(textColor)
                        Text(stringResource(it.label), color = colorState)
                    },
                    selected = selected,
                    onClick = { currentDestination = it },
                    alwaysShowLabel = navSuiteType == NavigationSuiteType.NavigationBar,
                    colors = suiteItemColors
                )
            }
        },
    ) {
        when (currentDestination) {
            NavDestinations.HOME -> PageHome()
            NavDestinations.CATEGORY -> PageTree()
            NavDestinations.DISCOVERY -> PageDiscovery()
            NavDestinations.SYSTEM -> PageCategory()
            NavDestinations.MINE -> PageMine()
        }
    }
}
