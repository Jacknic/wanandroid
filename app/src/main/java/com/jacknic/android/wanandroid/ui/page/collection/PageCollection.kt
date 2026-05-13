package com.jacknic.android.wanandroid.ui.page.collection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jacknic.android.wanandroid.core.common.StateResult
import com.jacknic.android.wanandroid.core.model.Article
import com.jacknic.android.wanandroid.core.network.isUnauthorized
import com.jacknic.android.wanandroid.ui.component.ArticleListItem
import com.jacknic.android.wanandroid.ui.page.LocalNavCtrl
import com.jacknic.android.wanandroid.ui.page.Page
import com.jacknic.android.wanandroid.ui.page.navTop
import com.jacknic.android.wanandroid.ui.page.openBrowser

/**
 * 我的收藏页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageCollection(vm: CollectionViewModel = hiltViewModel()) {
    val nav = LocalNavCtrl.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val collectListState by vm.collectList.collectAsStateWithLifecycle()
    var isRefreshing by remember { mutableStateOf(false) }
    var articleToRemove by remember { mutableStateOf<Article?>(null) }

    val lazyListState = rememberLazyListState()

    // 滚动到底部自动加载更多
    LaunchedEffect(lazyListState) {
        snapshotFlow {
            val lastVisibleIndex =
                lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = lazyListState.layoutInfo.totalItemsCount
            lastVisibleIndex to totalItems
        }.collect { (lastVisible, total) ->
            if (lastVisible >= total - 2 && total > 0) {
                vm.loadMore()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("我的收藏") },
                navigationIcon = {
                    IconButton(onClick = { nav.navigateUp() }) {
                        Icon(Icons.AutoMirrored.TwoTone.ArrowBack, "返回")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                vm.refresh()
                isRefreshing = false
            },
            modifier = Modifier
                .padding(padding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            when (val state = collectListState) {
                is StateResult.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is StateResult.Error -> {
                    val isUnauthorized = state.exception?.isUnauthorized() ?: false
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                if (isUnauthorized) "未登录" else "加载失败",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            if (isUnauthorized) {
                                TextButton(onClick = { nav.navTop(Page.Login, Page.Main) }) {
                                    Text("去登录")
                                }
                            } else {
                                TextButton(onClick = { vm.refresh() }) {
                                    Text("重试")
                                }
                            }
                        }
                    }
                }

                is StateResult.Success -> {
                    val articles = state.data.datas
                    val total = state.data.total
                    if (articles.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "暂无收藏文章",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        LazyColumn(state = lazyListState) {
                            item {
                                Text(
                                    "共 $total 篇收藏",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                            items(articles, key = { it.id }) { article ->
                                CollectArticleItem(
                                    article = article,
                                    onClick = { nav.openBrowser(article.link) },
                                    onRemove = { articleToRemove = article }
                                )
                            }
                            if (!state.data.over) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                null -> {}
            }
        }
    }

    // 取消收藏确认对话框
    articleToRemove?.let { article ->
        AlertDialog(
            onDismissRequest = { articleToRemove = null },
            title = { Text("取消收藏") },
            text = {
                Text(
                    article.title.take(50)
                        .let { if (it.length < article.title.length) "$it…" else it },
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    vm.uncollect(article)
                    articleToRemove = null
                }) {
                    Text("确定", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { articleToRemove = null }) {
                    Text("取消")
                }
            }
        )
    }
}

@Composable
private fun CollectArticleItem(
    article: Article,
    onClick: () -> Unit,
    onRemove: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            ArticleListItem(
                article = article,
                onClick = onClick
            )
        }
        IconButton(
            onClick = onRemove,
            modifier = Modifier.padding(end = 4.dp)
        ) {
            Icon(
                Icons.TwoTone.Delete,
                contentDescription = "取消收藏",
                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
