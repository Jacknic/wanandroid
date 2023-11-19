package com.jacknic.android.wanandroid.ui.page.main

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AccountCircle
import androidx.compose.material.icons.twotone.DateRange
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.List
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import com.jacknic.android.wanandroid.R
import com.jacknic.android.wanandroid.ui.page.main.category.PageCategory
import com.jacknic.android.wanandroid.ui.page.main.discovery.PageDiscovery
import com.jacknic.android.wanandroid.ui.page.main.home.PageHome
import com.jacknic.android.wanandroid.ui.page.main.mine.PageMine
import com.jacknic.android.wanandroid.ui.page.main.tree.PageTree
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PageMain() {
    val pagerState = rememberPagerState(0)
    val selectedIndex = pagerState.currentPage
    val scope = rememberCoroutineScope()
    val tabTitles = stringArrayResource(R.array.tab_titles)
    val tabIcons = arrayOf(
        Icons.TwoTone.Home,
        Icons.TwoTone.List,
        Icons.TwoTone.Search,
        Icons.TwoTone.DateRange,
        Icons.TwoTone.AccountCircle
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
                        icon = { Icon(tabIcons[index], s) },
                        label = { Text(s) },
                        alwaysShowLabel = false
                    )
                }
            }
        }) { padding ->
        HorizontalPager(
            pageCount = tabTitles.size,
            Modifier.padding(padding),
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
