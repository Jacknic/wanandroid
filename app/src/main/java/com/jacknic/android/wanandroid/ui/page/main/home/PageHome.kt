package com.jacknic.android.wanandroid.ui.page.main.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.KeyboardArrowRight
import androidx.compose.material.icons.twotone.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.jacknic.android.wanandroid.ui.page.LocalNavCtrl

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PageHome(
    pageScrollBehavior: TopAppBarScrollBehavior,
    listState: LazyListState = rememberLazyListState(),
) {
    val nav = LocalNavCtrl.current
    val stackEntry = nav.currentBackStackEntry!!
    val vm: HomeViewModel = hiltViewModel(stackEntry)
    val pagingItems =
        vm.articleListFlow.collectAsLazyPagingItems(stackEntry.lifecycleScope.coroutineContext)
    LazyColumn(
        Modifier.nestedScroll(pageScrollBehavior.nestedScrollConnection),
        state = listState
    ) {
        item {
            Text("当前页面：", modifier = Modifier.clickable { vm.loadPagingData() })
        }
        item {
            HorizontalPager(
                pageCount = 5, modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(MaterialTheme.colorScheme.inversePrimary, CircleShape)
            ) {
                Image(Icons.TwoTone.ThumbUp, "", modifier = Modifier.fillMaxSize())
            }
        }
        items(pagingItems.itemCount) {
            val article = pagingItems[it] ?: return@items
            ListItem(
                leadingContent = { Text(article.title.firstOrNull().toString()) },
                headlineText = {
                    Text(
                        article.title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                trailingContent = {
                    Icon(Icons.TwoTone.KeyboardArrowRight, "")
                }
            )
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