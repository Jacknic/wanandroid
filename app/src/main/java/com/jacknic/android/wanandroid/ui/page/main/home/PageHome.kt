package com.jacknic.android.wanandroid.ui.page.main.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.jacknic.android.core.model.Banner
import com.jacknic.android.wanandroid.core.common.onSuccess
import com.jacknic.android.wanandroid.ui.component.ArticleListItem
import com.jacknic.android.wanandroid.ui.component.HomeBanner
import com.jacknic.android.wanandroid.ui.page.LocalNavCtrl
import com.jacknic.android.wanandroid.ui.page.Page

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PageHome(
    pageScrollBehavior: TopAppBarScrollBehavior,
    listStateTop: LazyListState = rememberLazyListState(),
) {
    val nav = LocalNavCtrl.current
    val stackEntry = nav.currentBackStackEntry!!
    val vm: HomeViewModel = hiltViewModel(stackEntry)
    val context = LocalContext.current
    val pagingItems = vm.articleListFlow.collectAsLazyPagingItems()
    val bannerResult = vm.bannerList.collectAsState()
    var banners = emptyList<Banner>()
    bannerResult.value.onSuccess {
        banners = it
    }
    val scrollConnection = if (pagingItems.itemCount > 0) pageScrollBehavior.nestedScrollConnection
    else rememberNestedScrollInteropConnection()
    val listState = if (pagingItems.itemCount > 0) listStateTop else rememberLazyListState()
    LazyColumn(
        Modifier
            .nestedScroll(scrollConnection)
            .fillMaxSize(1f)
            .background(MaterialTheme.colorScheme.onBackground.copy(0.05f)),
        state = listState
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
                            onClick = { pagingItems.retry() },
                            modifier = Modifier.height(50.dp)
                        ) {
                            Text("加载错误,点击重试")
                        }
                    }
                }
            }
        }
    }
}