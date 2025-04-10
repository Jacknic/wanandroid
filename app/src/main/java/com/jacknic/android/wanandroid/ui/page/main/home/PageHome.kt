package com.jacknic.android.wanandroid.ui.page.main.home

import android.annotation.SuppressLint
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
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
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PageHome() {
    val nav = LocalNavCtrl.current
    val stackEntry = nav.currentBackStackEntry!!
    val vm: HomeViewModel = hiltViewModel(stackEntry)
    val pagingItems = vm.articleListFlow.collectAsLazyPagingItems()
    val bannerResult by vm.bannerList.collectAsState()
    val banners = bannerResult.getDataOrNull() ?: emptyList()
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val listStateTop: LazyListState = rememberLazyListState()
    val scrollConnection = if (pagingItems.itemCount > 0) scrollBehavior.nestedScrollConnection
    else rememberNestedScrollInteropConnection()
    val listState = if (pagingItems.itemCount > 0) listStateTop else rememberLazyListState()
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
                .background(MaterialTheme.colorScheme.onBackground.copy(0.05f)), state = listState
        ) {
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