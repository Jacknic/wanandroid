package com.jacknic.android.wanandroid.ui.page.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.List
import androidx.compose.material.icons.twotone.AccountCircle
import androidx.compose.material.icons.twotone.DateRange
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCancellationBehavior
import com.airbnb.lottie.compose.LottieCompositionSpec.RawRes
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.jacknic.android.wanandroid.R
import com.jacknic.android.wanandroid.ui.page.main.category.PageCategory
import com.jacknic.android.wanandroid.ui.page.main.discovery.PageDiscovery
import com.jacknic.android.wanandroid.ui.page.main.home.PageHome
import com.jacknic.android.wanandroid.ui.page.main.mine.PageMine
import com.jacknic.android.wanandroid.ui.page.main.tree.PageTree
import kotlinx.coroutines.launch
import com.jacknic.android.wanandroid.core.ui.R as UR

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageMain() {
    val tabTitles = stringArrayResource(R.array.tab_titles)
    val pagerState = rememberPagerState { tabTitles.size }
    val selectedIndex = pagerState.currentPage
    val scope = rememberCoroutineScope()
    val tabIcons = arrayOf(
        Icons.TwoTone.Home,
        Icons.AutoMirrored.TwoTone.List,
        Icons.TwoTone.Search,
        Icons.TwoTone.DateRange,
        Icons.TwoTone.AccountCircle
    )
    val tabIconRes = arrayOf(
        UR.raw.tabbar_animate_course,
        UR.raw.tabbar_animate_discover,
        UR.raw.tabbar_animate_dynamic,
        UR.raw.tabbar_animate_home,
        UR.raw.tabbar_animate_mine,
    )
    val themeColor = MaterialTheme.colorScheme.primary
    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars,
        bottomBar = {
            NavigationBar {
                tabTitles.forEachIndexed { index, s ->
                    val selected = index == selectedIndex
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            scope.launch {
                                pagerState.scrollToPage(index)
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = themeColor,
                            selectedTextColor = themeColor
                        ),
                        icon = {
                            val composition by rememberLottieComposition(RawRes(tabIconRes[index]))
                            val progress by animateLottieCompositionAsState(
                                composition,
                                speed = if (selected) 1f else 0.5f,
                                isPlaying = selected,
                                cancellationBehavior = LottieCancellationBehavior.Immediately
                            )
                            LottieAnimation(
                                modifier = Modifier.size(24.dp),
                                composition = composition,
                                progress = { if (selected) progress else 0f }
                            )
                        },
                        label = { Text(s) },
                        alwaysShowLabel = true
                    )
                }
            }
        }) { padding ->
        HorizontalPager(
            modifier = Modifier.padding(padding),
            key = { index -> index },
            state = pagerState
        ) {
            when (it) {
                0 -> PageHome()
                1 -> PageTree()
                2 -> PageDiscovery()
                3 -> PageCategory()
                4 -> PageMine()
                else -> {}
            }
        }
    }
}
