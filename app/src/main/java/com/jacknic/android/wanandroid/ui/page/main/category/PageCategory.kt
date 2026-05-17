@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.jacknic.android.wanandroid.ui.page.main.category

import android.os.Parcelable
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.twotone.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.parseAsHtml
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.jacknic.android.wanandroid.R
import com.jacknic.android.wanandroid.core.common.StateResult
import com.jacknic.android.wanandroid.core.common.getDataOrNull
import com.jacknic.android.wanandroid.core.model.Article
import com.jacknic.android.wanandroid.core.model.Tree
import com.jacknic.android.wanandroid.ui.component.ArticleListItem
import com.jacknic.android.wanandroid.ui.component.CollectResult
import com.jacknic.android.wanandroid.ui.page.LocalCollectStateManager
import com.jacknic.android.wanandroid.ui.page.LocalNavCtrl
import com.jacknic.android.wanandroid.ui.page.Page
import com.jacknic.android.wanandroid.ui.page.navTop
import com.jacknic.android.wanandroid.ui.page.openBrowser
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

/**
 * 体系页导航键
 */
@Parcelize
data class TreeNavKey(
    val id: Int,
    val name: String,
    val parentName: String
) : Parcelable

/**
 * 体系页
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageCategory(
    scaffoldNavigator: ThreePaneScaffoldNavigator<TreeNavKey> = rememberListDetailPaneScaffoldNavigator<TreeNavKey>(),
    vm: CategoryViewModel = hiltViewModel()
) {
    val nav = LocalNavCtrl.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val collectStateManager = LocalCollectStateManager.current
    val collectIds by collectStateManager.collectIds.collectAsState()
    val collectInitialized by collectStateManager.isInitialized.collectAsState()
    var showLoginDialog by remember { mutableStateOf(false) }
    val treeResult by vm.treeResult.collectAsState()
    val searchQuery by vm.searchQuery.collectAsState()
    val expandedIndices by vm.expandedIndices.collectAsState()

    val trees = treeResult.getDataOrNull() ?: emptyList()
    val filteredTrees = remember(trees, searchQuery) { vm.filterTrees(trees) }
    val isLoading = treeResult is StateResult.Loading
    val isError = treeResult is StateResult.Error

    val savedTreeScroll = vm.getTreeScrollState()
    val treeListState = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState(
            firstVisibleItemIndex = savedTreeScroll.first,
            firstVisibleItemScrollOffset = savedTreeScroll.second
        )
    }

    LaunchedEffect(treeListState) {
        snapshotFlow {
            treeListState.firstVisibleItemIndex to treeListState.firstVisibleItemScrollOffset
        }.collect { (index, offset) ->
            vm.saveTreeScrollState(index, offset)
        }
    }

    val currentNavKey = scaffoldNavigator.currentDestination?.contentKey

    // 返回按钮拦截：详情面板可见时，按返回键回到列表而非退出页面
    BackHandler(enabled = scaffoldNavigator.canNavigateBack()) {
        scope.launch { scaffoldNavigator.navigateBack() }
    }

    ListDetailPaneScaffold(
        directive = scaffoldNavigator.scaffoldDirective,
        value = scaffoldNavigator.scaffoldValue,
        listPane = {
            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
            var isSearching by rememberSaveable { mutableStateOf(vm.getIsSearching()) }

            LaunchedEffect(isSearching) {
                vm.saveIsSearching(isSearching)
            }

            Column(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                TopAppBar(
                    title = {
                        if (isSearching) {
                            TextField(
                                value = searchQuery,
                                onValueChange = { vm.updateSearchQuery(it) },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("搜索分类") },
                                leadingIcon = {
                                    Icon(Icons.Default.Search, contentDescription = null)
                                },
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                )
                            )
                        } else {
                            Text(stringResource(R.string.title_category))
                        }
                    },
                    actions = {
                        if (isSearching) {
                            IconButton(onClick = {
                                isSearching = false
                                vm.updateSearchQuery("")
                            }) {
                                Icon(Icons.Default.Close, contentDescription = "关闭搜索")
                            }
                        } else {
                            IconButton(onClick = { isSearching = true }) {
                                Icon(Icons.Default.Search, contentDescription = "搜索")
                            }
                        }
                    },
                    scrollBehavior = scrollBehavior,
                    colors = TopAppBarDefaults.topAppBarColors()
                        .copy(scrolledContainerColor = MaterialTheme.colorScheme.surface)
                )

                // 加载状态
                AnimatedVisibility(visible = isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }

                // 错误状态
                AnimatedVisibility(visible = isError) {
                    ErrorView(
                        message = "加载体系数据失败",
                        onRetry = { vm.refresh() }
                    )
                }
                val skc = LocalSoftwareKeyboardController.current
                // 体系列表
                if (!isLoading && !isError) {
                    TreeList(
                        trees = filteredTrees,
                        expandedIndices = expandedIndices,
                        listState = treeListState,
                        onToggleExpand = { vm.toggleExpand(it) },
                        onSelectChild = { chapterId, name, parentName ->
                            vm.selectChild(chapterId)
                            scope.launch {
                                skc?.hide()
                                scaffoldNavigator.navigateTo(
                                    ListDetailPaneScaffoldRole.Detail,
                                    TreeNavKey(chapterId, name, parentName)
                                )
                            }
                        })
                }
            }
        },
        detailPane = {
            if (currentNavKey != null) {
                TreeDetailPane(
                    navKey = currentNavKey,
                    vm = vm,
                    onBack = {
                        scope.launch { scaffoldNavigator.navigateBack() }
                    },
                    onArticleClick = { article -> nav.openBrowser(article.link) },
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
            } else {
                // 未选中任何分类时的占位
                EmptyDetailView()
            }
        }
    )

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

/**
 * 体系树列表（可展开/折叠）
 */
@Composable
private fun TreeList(
    trees: List<Tree>,
    expandedIndices: Set<Int>,
    listState: LazyListState,
    onToggleExpand: (Int) -> Unit,
    onSelectChild: (chapterId: Int, name: String, parentName: String) -> Unit,
) {
    if (trees.isEmpty()) {
        EmptyView(message = "暂无匹配的分类")
        return
    }

    LazyColumn(state = listState) {
        trees.forEachIndexed { index, tree ->
            val isExpanded = expandedIndices.contains(index)
            val displayName = tree.name.parseAsHtml().toString()

            // 一级分类
            item(key = "parent_${tree.id}") {
                ListItem(
                    headlineContent = {
                        Text(
                            text = displayName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    trailingContent = {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown
                            else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = if (isExpanded) "折叠" else "展开"
                        )
                    },
                    modifier = Modifier.clickable { onToggleExpand(index) }
                )
            }

            // 二级分类（展开时显示）
            if (isExpanded) {
                items(
                    count = tree.children.size,
                    key = { childIndex -> "child_${tree.children[childIndex].id}" }
                ) { childIndex ->
                    val child = tree.children[childIndex]
                    val childName = child.name.parseAsHtml().toString()

                    ListItem(
                        headlineContent = {
                            Text(
                                text = childName,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        },
                        modifier = Modifier.clickable {
                            onSelectChild(child.id, childName, displayName)
                        }
                    )
                }
            }
        }
    }
}

/**
 * 详情面板 - 文章列表
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TreeDetailPane(
    navKey: TreeNavKey,
    vm: CategoryViewModel,
    onBack: () -> Unit,
    onArticleClick: (Article) -> Unit,
    onCollectClick: (Article, Boolean) -> Unit = { _, _ -> },
    collectIds: Set<Int> = emptySet(),
    collectInitialized: Boolean = false,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val pagingItems = vm.getArticleListFlow(navKey.id).collectAsLazyPagingItems()

    val savedDetailScroll = vm.getDetailScrollState(navKey.id)
    val detailGridState = rememberSaveable(saver = LazyGridState.Saver) {
        LazyGridState(
            firstVisibleItemIndex = savedDetailScroll.first,
            firstVisibleItemScrollOffset = savedDetailScroll.second
        )
    }

    LaunchedEffect(detailGridState) {
        snapshotFlow {
            detailGridState.firstVisibleItemIndex to detailGridState.firstVisibleItemScrollOffset
        }.collect { (index, offset) ->
            vm.saveDetailScrollState(navKey.id, index, offset)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = navKey.parentName + " / " + navKey.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) { paddingValues ->
        ArticlePagingList(
            pagingItems = pagingItems,
            onArticleClick = onArticleClick,
            onCollectClick = onCollectClick,
            collectIds = collectIds,
            collectInitialized = collectInitialized,
            gridState = detailGridState,
            modifier = Modifier
                .padding(paddingValues)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        )
    }
}

/**
 * 分页文章列表
 */
@Composable
private fun ArticlePagingList(
    pagingItems: LazyPagingItems<Article>,
    onArticleClick: (Article) -> Unit,
    onCollectClick: (Article, Boolean) -> Unit = { _, _ -> },
    collectIds: Set<Int> = emptySet(),
    collectInitialized: Boolean = false,
    gridState: LazyGridState,
    modifier: Modifier = Modifier
) {
    val loadState = pagingItems.loadState

    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = modifier.fillMaxSize(),
        state = gridState,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
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

        // 加载状态处理
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
                        message = "加载文章失败",
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
                    EmptyView(message = "该分类暂无文章")
                }
            }

            loadState.append is LoadState.NotLoading
                    && (loadState.append as LoadState.NotLoading).endOfPaginationReached
                    && pagingItems.itemCount > 0 -> {
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

/**
 * 详情面板空状态
 */
@Composable
private fun EmptyDetailView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.statusBars.asPaddingValues()),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "请选择一个分类",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "从左侧列表选择分类查看文章",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
    }
}

/**
 * 错误视图
 */
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
            Spacer(modifier = Modifier.width(6.dp))
            Text("重试")
        }
    }
}

/**
 * 空状态视图
 */
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
