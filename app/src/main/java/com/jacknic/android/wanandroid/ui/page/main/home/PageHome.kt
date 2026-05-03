package com.jacknic.android.wanandroid.ui.page.main.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AccountCircle
import androidx.compose.material.icons.twotone.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.jacknic.android.wanandroid.core.common.StateResult
import com.jacknic.android.wanandroid.core.common.getDataOrNull
import com.jacknic.android.wanandroid.ui.component.ArticleListItem
import com.jacknic.android.wanandroid.ui.component.HomeBanner
import com.jacknic.android.wanandroid.ui.component.SearchBar
import com.jacknic.android.wanandroid.ui.page.LocalNavCtrl
import com.jacknic.android.wanandroid.ui.page.Page
import kotlinx.coroutines.launch

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
    val bannerResult by vm.bannerList.collectAsState()
    val categoryResult by vm.categories.collectAsState()
    val banners = bannerResult.getDataOrNull() ?: emptyList()
    val categories = categoryResult.getDataOrNull() ?: emptyList()

    val pagerState = rememberPagerState(pageCount = { categories.size.coerceAtLeast(1) })
    val scope = rememberCoroutineScope()

    val containerColor = MaterialTheme.colorScheme.surfaceContainer
    val isLoading = bannerResult is StateResult.Loading || categoryResult is StateResult.Loading
    val isError = bannerResult is StateResult.Error || categoryResult is StateResult.Error

    Scaffold(
        containerColor = containerColor,
        topBar = {
            TopAppBar(
                title = {
                    SearchBar(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { nav.navigate(Page.Search) }
                    )
                },
                actions = {
                    IconButton(onClick = { nav.navigate(Page.Search) }) {
                        Icon(Icons.TwoTone.AccountCircle, "我的")
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(scrolledContainerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize()
        ) {
            // 加载状态
            AnimatedVisibility(visible = isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            // 错误状态
            AnimatedVisibility(visible = isError) {
                ErrorView(
                    message = "加载失败",
                    onRetry = { vm.refresh() }
                )
            }

            // 分类标签栏
            AnimatedVisibility(visible = categories.isNotEmpty() && !isLoading && !isError) {
                SecondaryScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    modifier = Modifier.fillMaxWidth(),
                    edgePadding = 1.dp,
                    containerColor = MaterialTheme.colorScheme.surface,
                    divider = {},
                    indicator = {
                        Box(
                            Modifier
                                .tabIndicatorOffset(pagerState.currentPage, true)
                                .height(3.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp)
                                )
                        )
                    }
                ) {
                    categories.forEachIndexed { index, category ->
                        Tab(
                            selected = index == pagerState.currentPage,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            text = {
                                Text(
                                    text = HtmlCompat.fromHtml(
                                        category.name,
                                        HtmlCompat.FROM_HTML_MODE_LEGACY
                                    ).toString(),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = if (index == pagerState.currentPage) FontWeight.SemiBold else FontWeight.Normal,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            selectedContentColor = MaterialTheme.colorScheme.primary,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // 内容区域
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { pageIndex ->
                if (categories.isEmpty()) return@HorizontalPager

                val category = categories[pageIndex]
                val pagingItems = vm.getArticleListFlow(category.id).collectAsLazyPagingItems()

                ArticleList(
                    pagingItems = pagingItems,
                    showBanner = pageIndex == 0 && banners.isNotEmpty(),
                    banners = banners,
                    onArticleClick = { article ->
                        nav.currentBackStackEntry?.savedStateHandle?.set("link", article.link)
                        nav.navigate(Page.Browser)
                    }
                )
            }
        }
    }
}

@Composable
private fun ArticleList(
    pagingItems: androidx.paging.compose.LazyPagingItems<com.jacknic.android.wanandroid.core.model.Article>,
    showBanner: Boolean,
    banners: List<com.jacknic.android.wanandroid.core.model.Banner>,
    onArticleClick: (com.jacknic.android.wanandroid.core.model.Article) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // Banner
        if (showBanner) {
            item {
                HomeBanner(banners)
            }
        }

        // 文章列表
        items(
            count = pagingItems.itemCount,
            key = { index -> pagingItems[index]?.id ?: index }
        ) { index ->
            val article = pagingItems[index] ?: return@items
            ArticleListItem(
                article = article,
                onClick = { onArticleClick(article) }
            )
        }

        // 加载状态
        val state = pagingItems.loadState
        when {
            state.refresh is LoadState.Loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(32.dp))
                    }
                }
            }

            state.refresh is LoadState.Error -> {
                item {
                    ErrorView(
                        message = "加载失败",
                        onRetry = { pagingItems.refresh() }
                    )
                }
            }

            state.append is LoadState.Loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    }
                }
            }

            state.append is LoadState.Error -> {
                item {
                    ErrorView(
                        message = "加载更多失败",
                        onRetry = { pagingItems.retry() }
                    )
                }
            }

            pagingItems.itemCount == 0 && state.refresh is LoadState.NotLoading -> {
                item {
                    EmptyView(message = "暂无文章")
                }
            }
        }
    }
}

@Composable
private fun ErrorView(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Button(onClick = onRetry) {
            Icon(Icons.TwoTone.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text("重试")
        }
    }
}

@Composable
private fun EmptyView(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
