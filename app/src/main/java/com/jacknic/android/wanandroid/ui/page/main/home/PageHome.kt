package com.jacknic.android.wanandroid.ui.page.main.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.jacknic.android.wanandroid.core.common.getDataOrNull
import com.jacknic.android.wanandroid.ui.component.ArticleListItem
import com.jacknic.android.wanandroid.ui.component.HomeBanner
import com.jacknic.android.wanandroid.ui.page.LocalNavCtrl
import com.jacknic.android.wanandroid.ui.page.Page

/**
 * 首页
 *
 * @author Jacknic
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PageHome(
    vm: HomeViewModel = hiltViewModel(),
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    listStateTop: LazyListState = rememberLazyListState()
) {
    val nav = LocalNavCtrl.current
    val pagingItems = vm.articleListFlow.collectAsLazyPagingItems()
    val bannerResult by vm.bannerList.collectAsState()
    val banners = bannerResult.getDataOrNull() ?: emptyList()
    val scrollConnection = scrollBehavior.nestedScrollConnection
    var tabIndex by rememberSaveable { mutableIntStateOf(0) }
    Scaffold(topBar = {
        TopAppBar(
            title = {},
            actions = {
                IconButton(onClick = {
                    nav.navigate(Page.Search)
                }) {
                    Icon(Icons.TwoTone.Search, "搜索")
                }
            },
            scrollBehavior = scrollBehavior
        )
    }) {
        LazyColumn(
            Modifier
                .padding(it)
                .nestedScroll(scrollConnection)
                .fillMaxSize(1f)
                .background(MaterialTheme.colorScheme.onBackground.copy(0.05f)),
            state = listStateTop
        ) {
            stickyHeader {
                PrimaryScrollableTabRow(
                    tabIndex,
                    modifier = Modifier.fillParentMaxWidth(),
                    edgePadding = 8.dp,
                    divider = {}
                ) {
                    for (i in 0 until 15) {
                        Tab(tabIndex == i, onClick = {
                            tabIndex = i
                        }) {
                            Text(
                                "Tab内容 $i",
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(12.dp, 8.dp)
                            )
                        }
                    }
                }
            }
            item {
                HomeBanner(banners)
            }
            items(pagingItems.itemCount) {
                val article = pagingItems[it] ?: return@items
                Spacer(modifier = Modifier.size(8.dp))
                ArticleListItem(article) {
                    nav.currentBackStackEntry?.savedStateHandle?.set("link", article.link)
                    nav.navigate(Page.Browser)
                }
            }
            val state = pagingItems.loadState
            val modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
            when {
                state.refresh is LoadState.Loading || state.append is LoadState.Loading -> {
                    item {
                        Box(modifier = modifier, contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }

                state.refresh is LoadState.Error -> {
                    item {
                        Box(modifier = modifier, contentAlignment = Alignment.Center) {
                            Button(
                                onClick = { pagingItems.refresh() },
                                modifier = Modifier.height(50.dp)
                            ) {
                                Text("加载错误,重新加载")
                            }
                        }
                    }
                }

                state.append is LoadState.Error -> {
                    item {
                        Box(modifier = modifier, contentAlignment = Alignment.Center) {
                            Button(
                                onClick = { pagingItems.retry() }, modifier = Modifier.height(50.dp)
                            ) {
                                Text("加载错误,点击重试")
                            }
                        }
                    }
                }
            }
        }
    }
}