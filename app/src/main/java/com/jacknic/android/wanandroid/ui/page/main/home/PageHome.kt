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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.parseAsHtml
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.jacknic.android.wanandroid.core.common.StateResult
import com.jacknic.android.wanandroid.core.common.getDataOrNull
import com.jacknic.android.wanandroid.core.model.Article
import com.jacknic.android.wanandroid.core.model.Banner
import com.jacknic.android.wanandroid.ui.component.ArticleListItem
import com.jacknic.android.wanandroid.ui.component.HomeBanner
import com.jacknic.android.wanandroid.ui.component.SearchBar
import com.jacknic.android.wanandroid.ui.page.LocalNavCtrl
import com.jacknic.android.wanandroid.ui.page.Page
import com.jacknic.android.wanandroid.ui.page.openBrowser
import kotlinx.coroutines.launch

/**
 * 首页
 *
 * @author Jacknic
 */
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
)
@Composable
fun PageHome(
    vm: HomeViewModel = hiltViewModel(),
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
) {
    val nav = LocalNavCtrl.current
    val bannerResult by vm.bannerList.collectAsState()
    val categoryResult by vm.categories.collectAsState()
    val banners = bannerResult.getDataOrNull() ?: emptyList()
    val categories = categoryResult.getDataOrNull() ?: emptyList()

    // 预处理分类名称，避免在测量过程中解析 HTML 导致 layout state 异常
    val categoryNames = remember(categories) {
        categories.map {
            it.name.parseAsHtml().toString()
        }
    }

    val pagerState = rememberPagerState(pageCount = { categories.size.coerceAtLeast(1) })
    val scope = rememberCoroutineScope()

    val containerColor = MaterialTheme.colorScheme.surfaceContainer
    val isLoading = bannerResult is StateResult.Loading || categoryResult is StateResult.Loading
    val isError = bannerResult is StateResult.Error || categoryResult is StateResult.Error

    // 计算屏幕宽度是否为宽屏
    val windowSizeClass = currentWindowAdaptiveInfo()
    val isWideScreen = windowSizeClass.windowSizeClass.isWidthAtLeastBreakpoint(600)

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
                                .tabIndicatorOffset(pagerState.currentPage, false)
                                .height(3.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp)
                                )
                        )
                    }
                ) {
                    categories.forEachIndexed { index, _ ->
                        Tab(
                            selected = index == pagerState.currentPage,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            text = {
                                Text(
                                    text = categoryNames.getOrElse(index) { "" },
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
                modifier = Modifier.weight(1f),
                key = { pageIndex -> pageIndex }
            ) { pageIndex ->
                if (categories.isEmpty()) return@HorizontalPager

                val category = categories[pageIndex]
                val pagingItems = vm.getArticleListFlow(category.id).collectAsLazyPagingItems()
                val gridState = rememberSaveable(saver = LazyGridState.Saver) { LazyGridState() }

                ArticleList(
                    pagingItems = pagingItems,
                    showBanner = pageIndex == 0 && banners.isNotEmpty(),
                    banners = banners,
                    isWideScreen = isWideScreen,
                    gridState = gridState,
                    onArticleClick = { article ->
                        nav.openBrowser(article.link)
                    }
                )
            }
        }
    }
}

@Composable
private fun ArticleList(
    pagingItems: LazyPagingItems<Article>,
    showBanner: Boolean,
    banners: List<Banner>,
    isWideScreen: Boolean,
    gridState: LazyGridState,
    onArticleClick: (Article) -> Unit,
) {
    val loadState = pagingItems.loadState

    LazyVerticalGrid(
        columns = if (isWideScreen) GridCells.Adaptive(minSize = 300.dp) else GridCells.Fixed(1),
        modifier = Modifier.fillMaxSize(),
        state = gridState,
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Banner - 占满
        if (showBanner) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                HomeBanner(banners)
            }
        }

        // 文章网格
        items(
            count = pagingItems.itemCount,
            key = { index ->
                val article = pagingItems.peek(index)
                if (article != null) "${article.id}_$index" else "placeholder_$index"
            }
        ) { index ->
            val article = pagingItems[index] ?: return@items
            ArticleListItem(
                article = article,
                onClick = { onArticleClick(article) }
            )
        }

        // 加载状态
        when {
            loadState.refresh is LoadState.Loading -> {
                item(span = { GridItemSpan(maxLineSpan) }) {
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

            loadState.refresh is LoadState.Error -> {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    ErrorView(
                        message = "加载失败",
                        onRetry = { pagingItems.refresh() }
                    )
                }
            }

            loadState.append is LoadState.Loading -> {
                item(span = { GridItemSpan(maxLineSpan) }) {
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

            loadState.append is LoadState.Error -> {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    ErrorView(
                        message = "加载更多失败",
                        onRetry = { pagingItems.retry() }
                    )
                }
            }

            pagingItems.itemCount == 0 && loadState.refresh is LoadState.NotLoading -> {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    EmptyView(message = "暂无文章")
                }
            }

            loadState.append is LoadState.NotLoading && (loadState.append as LoadState.NotLoading).endOfPaginationReached && pagingItems.itemCount > 0 -> {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "已加载全部",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
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
