package com.jacknic.android.wanandroid.ui.page.home

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AccountCircle
import androidx.compose.material.icons.twotone.DateRange
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import com.jacknic.android.wanandroid.R
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PageHome() {

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
    Scaffold(topBar = {
        TopAppBar(
            modifier = Modifier.background(
                Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.inversePrimary
                    )
                )
            ),
            title = {
                Text(tabTitles[selectedIndex])
            },
            scrollBehavior = scrollBehavior.value,
            colors = TopAppBarDefaults.smallTopAppBarColors(),
        )
    }, bottomBar = {
        NavigationBar {
            tabTitles.forEachIndexed { index, s ->
                val selected = index == selectedIndex
                NavigationBarItem(selected = selected,
                    onClick = {
                        scope.launch {
                            pagerState.scrollToPage(index)
                        }
                    },
                    icon = { Icon(tabIcons[index], s) },
                    label = { Text(s) }
                )
            }
        }
    }) {
        HorizontalPager(
            pageCount = tabTitles.size,
            Modifier.padding(it),
            beyondBoundsPageCount = tabTitles.size - 1,
            key = { index -> index },
            state = pagerState
        ) {
            val pageScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            val listState = rememberLazyListState()
            if (pagerState.currentPage == it) {
                scrollBehavior.value = pageScrollBehavior
                LazyColumn(
                    Modifier.nestedScroll(pageScrollBehavior.nestedScrollConnection),
                    state = listState
                ) {
                    item {
                        Text("当前页面：$it")
                    }
                    items(100) {
                        ListItem(headlineText = { Text("当前显示的数据 $it") })
                    }
                }
            }

        }
    }

}