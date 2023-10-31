package com.jacknic.android.wanandroid.ui.page.main

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import com.jacknic.android.wanandroid.R
import com.jacknic.android.wanandroid.ui.page.LocalNavCtrl
import com.jacknic.android.wanandroid.ui.page.Page
import com.jacknic.android.wanandroid.ui.page.main.home.PageHome
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PageMain() {
    val nav = LocalNavCtrl.current
    val pagerState = rememberPagerState(0)
    val selectedIndex = pagerState.currentPage
    val scope = rememberCoroutineScope()
    val tabTitles = stringArrayResource(R.array.tab_titles)
    val tabIcons = arrayOf(
        Icons.TwoTone.Home,
        Icons.TwoTone.List,
        Icons.TwoTone.DateRange,
        Icons.TwoTone.AccountCircle
    )
    val scrollBehavior = remember {
        mutableStateOf<TopAppBarScrollBehavior?>(null)
    }
    val context = LocalContext.current
    val themeColor = MaterialTheme.colorScheme.primary
    Scaffold(topBar = {
        TopAppBar(
            modifier = Modifier.background(
                Brush.linearGradient(
                    colors = listOf(
                        themeColor,
                        MaterialTheme.colorScheme.inversePrimary
                    )
                )
            ),
            title = {
                Text(tabTitles[selectedIndex])
            },
            actions = {
                IconButton(onClick = {
                    nav.navigate(Page.Search)
                }) {
                    Icon(Icons.TwoTone.Search, "搜索")
                }
            },
            scrollBehavior = scrollBehavior.value,
            colors = TopAppBarDefaults.smallTopAppBarColors(),
        )
    }, bottomBar = {
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
            beyondBoundsPageCount = tabTitles.size - 1,
            key = { index -> index },
            state = pagerState
        ) {
            val pageScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            if (pagerState.currentPage == it) {
                scrollBehavior.value = pageScrollBehavior
            }
            PageHome(pageScrollBehavior)
        }
    }
}
