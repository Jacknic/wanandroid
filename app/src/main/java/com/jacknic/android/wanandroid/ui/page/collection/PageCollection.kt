package com.jacknic.android.wanandroid.ui.page.collection

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jacknic.android.wanandroid.core.common.StateResult
import com.jacknic.android.wanandroid.core.model.Article
import com.jacknic.android.wanandroid.core.network.isUnauthorized
import com.jacknic.android.wanandroid.core.ui.R
import com.jacknic.android.wanandroid.ui.component.ArticleListItem
import com.jacknic.android.wanandroid.ui.component.CollectResult
import com.jacknic.android.wanandroid.ui.page.LocalNavCtrl
import com.jacknic.android.wanandroid.ui.page.LocalReadingHistoryManager
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
    val readingHistoryManager = LocalReadingHistoryManager.current
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val collectListState by vm.collectList.collectAsStateWithLifecycle()
    var isRefreshing by remember { mutableStateOf(false) }
    var articleToRemove by remember { mutableStateOf<Article?>(null) }

    val isWideScreen = currentWindowAdaptiveInfo()
        .windowSizeClass.isWidthAtLeastBreakpoint(600)

    val lazyGridState = rememberSaveable(saver = LazyGridState.Saver) {
        LazyGridState()
    }

    // 滚动到底部自动加载更多
    LaunchedEffect(lazyGridState) {
        snapshotFlow {
            val lastVisibleIndex =
                lazyGridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = lazyGridState.layoutInfo.totalItemsCount
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
                title = { Text(stringResource(R.string.page_collection)) },
                navigationIcon = {
                    IconButton(onClick = { nav.navigateUp() }) {
                        Icon(Icons.AutoMirrored.TwoTone.ArrowBack, stringResource(R.string.action_back))
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
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
                .nestedScroll(scrollBehavior.nestedScrollConnection),
        ) {
            when (val state = collectListState) {
                is StateResult.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is StateResult.Error -> {
                    val isUnauthorized = state.exception?.isUnauthorized() ?: false
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                if (isUnauthorized) {
                                    stringResource(
                                        R.string.error_not_logged_in,
                                    )
                                } else {
                                    stringResource(R.string.error_unknown)
                                },
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            if (isUnauthorized) {
                                TextButton(onClick = { nav.navTop(Page.Login, Page.Main) }) {
                                    Text(stringResource(R.string.action_login))
                                }
                            } else {
                                TextButton(onClick = { vm.refresh() }) {
                                    Text(stringResource(R.string.action_retry))
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
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                stringResource(R.string.collection_empty),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = if (isWideScreen) GridCells.Adaptive(minSize = 300.dp) else GridCells.Fixed(1),
                            state = lazyGridState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Surface(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        stringResource(R.string.collection_total_count, total),
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(
                                            horizontal = 16.dp,
                                            vertical = 8.dp,
                                        ),
                                    )
                                }
                            }
                            itemsIndexed(
                                articles,
                                key = { index, article -> "${article.id}_$index" },
                            ) { _, article ->
                                CollectArticleItem(
                                    article = article,
                                    onClick = {
                                        readingHistoryManager.addReadingHistory(article)
                                        nav.openBrowser(article.link)
                                    },
                                    onRemove = { articleToRemove = article },
                                )
                            }
                            if (!state.data.over) {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center,
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
            title = { Text(stringResource(R.string.collection_cancel_title)) },
            text = {
                Text(
                    article.title.take(50)
                        .let { if (it.length < article.title.length) "$it…" else it },
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    vm.uncollect(article) { result ->
                        if (result is CollectResult.NotLoggedIn) {
                            nav.navTop(Page.Login, Page.Main)
                        } else if (result is CollectResult.Error) {
                            Toast.makeText(context, context.getString(result.errorResId), Toast.LENGTH_SHORT).show()
                        }
                    }
                    articleToRemove = null
                }) {
                    Text(stringResource(R.string.action_confirm), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { articleToRemove = null }) {
                    Text(stringResource(R.string.action_cancel))
                }
            },
        )
    }
}

@Composable
private fun CollectArticleItem(article: Article, onClick: () -> Unit, onRemove: () -> Unit) {
    ArticleListItem(
        article = article,
        isCollected = true,
        onCollectClick = onRemove,
        onClick = onClick,
    )
}
