package com.jacknic.android.wanandroid.ui.page.main.home

import androidx.compose.animation.AnimatedVisibility
import android.widget.Toast
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
import androidx.compose.material.icons.twotone.Refresh
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
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
import com.jacknic.android.wanandroid.ui.component.CollectResult
import com.jacknic.android.wanandroid.ui.component.CollectStateManager
import com.jacknic.android.wanandroid.ui.component.HomeBanner
import com.jacknic.android.wanandroid.ui.page.LocalCollectStateManager
import com.jacknic.android.wanandroid.ui.page.LocalNavCtrl
import com.jacknic.android.wanandroid.ui.page.Page
import com.jacknic.android.wanandroid.ui.page.navTop
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
    val context = LocalContext.current
    val collectStateManager = LocalCollectStateManager.current
    val collectIds by collectStateManager.collectIds.collectAsState()
    val collectInitialized by collectStateManager.isInitialized.collectAsState()
    val scope = rememberCoroutineScope()
    var showLoginDialog by remember { mutableStateOf(false) }
    val bannerResult by vm.bannerList.collectAsState()
    val categoryResult by vm.categories.collectAsState()
    val targetCid by vm.targetCid.collectAsState()
    val banners = bannerResult.getDataOrNull() ?: emptyList()
    val categories = categoryResult.getDataOrNull() ?: emptyList()

    // 预处理分类名称，避免在测量过程中解析 HTML 导致 layout state 异常
    val categoryNames = remember(categories) {
        categories.map {
            it.name.parseAsHtml().toString()
        }
    }

    val savedPage = vm.getSavedCurrentPage()
    val pagerState = rememberPagerState(initialPage = savedPage, pageCount = { categories.size.coerceAtLeast(1) })

    // 导航到目标分类 / 恢复已保存的页面
    val categoriesLoaded = categoryResult.getDataOrNull()?.isNotEmpty() == true
    LaunchedEffect(targetCid, categoriesLoaded) {
        if (!categoriesLoaded) return@LaunchedEffect
        val cats = categoryResult.getDataOrNull() ?: return@LaunchedEffect

        // 优先处理跨 Tab 的分类导航信号
        val cid = targetCid
        if (cid != null) {
            val index = cats.indexOfFirst { it.id == cid }
            if (index >= 0) {
                pagerState.scrollToPage(index)
            }
            vm.consumeTargetCid()
            return@LaunchedEffect
        }

        // 分类加载完成后，恢复已保存的页面位置
        val target = savedPage.coerceIn(0, cats.size - 1)
        if (pagerState.currentPage != target) {
            pagerState.scrollToPage(target)
        }
    }

    // 持续保存当前选中的分类页索引
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }
            .collect { page -> vm.saveCurrentPage(page) }
    }

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
                title = { },
                actions = {
                    IconButton(onClick = { nav.navigate(Page.Search) }) {
                        Icon(Icons.TwoTone.Search, "搜索")
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

                // 从 ViewModel 恢复滚动位置作为初始值
                val savedScroll = vm.getScrollState(category.id)
                val gridState = rememberSaveable(saver = LazyGridState.Saver) {
                    LazyGridState(
                        firstVisibleItemIndex = savedScroll.first,
                        firstVisibleItemScrollOffset = savedScroll.second
                    )
                }

                // 持续保存滚动位置到 ViewModel
                LaunchedEffect(gridState) {
                    snapshotFlow {
                        gridState.firstVisibleItemIndex to gridState.firstVisibleItemScrollOffset
                    }.collect { (index, offset) ->
                        vm.saveScrollState(category.id, index, offset)
                    }
                }

                ArticleList(
                    pagingItems = pagingItems,
                    showBanner = pageIndex == 0 && banners.isNotEmpty(),
                    banners = banners,
                    isWideScreen = isWideScreen,
                    gridState = gridState,
                    onArticleClick = { article ->
                        nav.openBrowser(article.link)
                    },
                    onCollectClick = { article, isCollected ->
                        scope.launch {
                            when (val result = collectStateManager.toggleCollect(article.id, isCollected)) {
                                is CollectResult.NotLoggedIn -> showLoginDialog = true
                                is CollectResult.Error -> Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                                is CollectResult.Success -> {}
                            }
                        }
                    },
                    collectIds = collectIds,
                    collectInitialized = collectInitialized
                )
            }
        }
    }

    // 未登录提示对话框
    if (showLoginDialog) {
        AlertDialog(
            onDismissRequest = { showLoginDialog = false },
            title = { Text("提示") },
            text = { Text("收藏功能需要登录，是否前往登录？") },
            confirmButton = {
                TextButton(onClick = {
                    showLoginDialog = false
                    nav.navTop(Page.Login, Page.Main)
                }) {
                    Text("去登录")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLoginDialog = false }) {
                    Text("取消")
                }
            }
        )
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
    onCollectClick: (Article, Boolean) -> Unit = { _, _ -> },
    collectIds: Set<Int> = emptySet(),
    collectInitialized: Boolean = false,
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
            val isCollected = if (collectInitialized) collectIds.contains(article.id) else article.collect
            ArticleListItem(
                article = article,
                isCollected = isCollected,
                onCollectClick = { onCollectClick(article, isCollected) },
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
