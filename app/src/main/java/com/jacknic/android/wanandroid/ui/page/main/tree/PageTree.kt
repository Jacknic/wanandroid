@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.jacknic.android.wanandroid.ui.page.main.tree

import android.os.Parcelable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.twotone.Refresh
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.jacknic.android.wanandroid.core.model.Tree
import com.jacknic.android.wanandroid.ui.component.ArticleListItem
import com.jacknic.android.wanandroid.ui.page.LocalNavCtrl
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
fun PageTree(
    scaffoldNavigator: ThreePaneScaffoldNavigator<TreeNavKey> = rememberListDetailPaneScaffoldNavigator<TreeNavKey>(),
    vm: TreeViewModel = hiltViewModel()
) {
    val nav = LocalNavCtrl.current
    val scope = rememberCoroutineScope()
    val treeResult by vm.treeResult.collectAsState()
    val searchQuery by vm.searchQuery.collectAsState()
    val expandedIndices by vm.expandedIndices.collectAsState()
    val selectedChildId by vm.selectedChildId.collectAsState()

    val trees = treeResult.getDataOrNull() ?: emptyList()
    val filteredTrees = remember(trees, searchQuery) { vm.filterTrees(trees) }
    val isLoading = treeResult is StateResult.Loading
    val isError = treeResult is StateResult.Error

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

            Column(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                TopAppBar(
                    title = { Text("体系") },
                    scrollBehavior = scrollBehavior,
                    colors = TopAppBarDefaults.topAppBarColors()
                        .copy(scrolledContainerColor = MaterialTheme.colorScheme.surface)
                )

                // 搜索栏
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { vm.updateSearchQuery(it) },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
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

                // 体系列表
                if (!isLoading && !isError) {
                    TreeList(
                        trees = filteredTrees,
                        expandedIndices = expandedIndices,
                        onToggleExpand = { vm.toggleExpand(it) },
                        onSelectChild = { chapterId, name, parentName ->
                            vm.selectChild(chapterId)
                            scope.launch {
                                scaffoldNavigator.navigateTo(
                                    ListDetailPaneScaffoldRole.Detail,
                                    TreeNavKey(chapterId, name, parentName)
                                )
                            }
                        },
                        contentPadding = WindowInsets.statusBars.asPaddingValues()
                    )
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
                    onArticleClick = { article -> nav.openBrowser(article.link) }
                )
            } else {
                // 未选中任何分类时的占位
                EmptyDetailView()
            }
        }
    )
}

/**
 * 搜索栏
 */
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp)),
        placeholder = { Text("搜索分类") },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null)
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(24.dp)
    )
}

/**
 * 体系树列表（可展开/折叠）
 */
@Composable
private fun TreeList(
    trees: List<Tree>,
    expandedIndices: Set<Int>,
    onToggleExpand: (Int) -> Unit,
    onSelectChild: (chapterId: Int, name: String, parentName: String) -> Unit,
    contentPadding: PaddingValues
) {
    if (trees.isEmpty()) {
        EmptyView(message = "暂无匹配的分类")
        return
    }

    LazyColumn(
        contentPadding = contentPadding
    ) {
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
    vm: TreeViewModel,
    onBack: () -> Unit,
    onArticleClick: (Article) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val pagingItems = vm.getArticleListFlow(navKey.id).collectAsLazyPagingItems()

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
    modifier: Modifier = Modifier
) {
    val loadState = pagingItems.loadState

    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = modifier.fillMaxSize(),
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
            ArticleListItem(
                article = article,
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
