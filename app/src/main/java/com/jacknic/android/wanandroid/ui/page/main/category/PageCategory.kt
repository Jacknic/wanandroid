package com.jacknic.android.wanandroid.ui.page.main.category

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.jacknic.android.wanandroid.core.model.Chapter
import com.jacknic.android.wanandroid.core.model.Tree
import com.jacknic.android.wanandroid.ui.component.ArticleListItem
import com.jacknic.android.wanandroid.ui.page.LocalNavCtrl
import com.jacknic.android.wanandroid.ui.page.openBrowser

/**
 * 分类页
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageCategory(
    vm: CategoryViewModel = hiltViewModel()
) {
    val nav = LocalNavCtrl.current
    val treeResult by vm.treeResult.collectAsState()
    val selectedParentIndex by vm.selectedParentIndex.collectAsState()
    val selectedChildId by vm.selectedChildId.collectAsState()

    val trees = treeResult.getDataOrNull() ?: emptyList()
    val isLoading = treeResult is StateResult.Loading
    val isError = treeResult is StateResult.Error

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    // 预解析分类名称
    val parentNames = remember(trees) {
        trees.map { it.name.parseAsHtml().toString() }
    }

    val currentChildren = remember(trees, selectedParentIndex) {
        trees.getOrElse(selectedParentIndex) { Tree() }.children
    }

    val childNames = remember(currentChildren) {
        currentChildren.map { it.name.parseAsHtml().toString() }
    }

    // 初始加载后自动选中第一个子分类
    LaunchedEffect(trees) {
        if (trees.isNotEmpty() && selectedChildId == null) {
            val firstChildren = trees.firstOrNull()?.children
            if (!firstChildren.isNullOrEmpty()) {
                vm.selectChild(firstChildren.first().id)
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            TopAppBar(
                title = { Text("分类") },
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
                    message = "加载分类数据失败",
                    onRetry = { vm.refresh() }
                )
            }

            // 主内容区
            AnimatedVisibility(visible = trees.isNotEmpty() && !isLoading && !isError) {
                Row(modifier = Modifier.fillMaxSize()) {
                    // 侧边栏 - 一级分类导航
                    CategorySidebar(
                        categories = parentNames,
                        selectedIndex = selectedParentIndex,
                        onCategoryClick = { index -> vm.selectParent(index) },
                        modifier = Modifier
                            .width(100.dp)
                            .fillMaxHeight()
                    )

                    // 右侧内容区
                    Column(modifier = Modifier.weight(1f)) {
                        // 二级分类标签栏
                        if (currentChildren.isNotEmpty()) {
                            SubCategoryTabs(
                                subCategories = currentChildren,
                                subCategoryNames = childNames,
                                selectedId = selectedChildId,
                                onSubCategoryClick = { chapter -> vm.selectChild(chapter.id) }
                            )
                        }

                        // 文章列表
                        val cid = selectedChildId
                        if (cid != null) {
                            val pagingItems = vm.getArticleListFlow(cid).collectAsLazyPagingItems()
                            CategoryArticleList(
                                pagingItems = pagingItems,
                                onArticleClick = { article ->
                                    nav.openBrowser(article.link)
                                }
                            )
                        } else {
                            EmptyView(message = "请选择分类")
                        }
                    }
                }
            }
        }
    }
}

/**
 * 侧边栏一级分类导航
 */
@Composable
private fun CategorySidebar(
    categories: List<String>,
    selectedIndex: Int,
    onCategoryClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    LazyColumn(
        modifier = modifier
            .background(colorScheme.surfaceContainerLow),
        contentPadding = PaddingValues(vertical = 4.dp)
    ) {
        itemsIndexed(categories) { index, name ->
            val isSelected = index == selectedIndex
            val bgShape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 1.dp, horizontal = 4.dp)
                    .clip(bgShape)
                    .background(
                        if (isSelected) colorScheme.surface
                        else colorScheme.surfaceContainerLow
                    )
                    .clickable { onCategoryClick(index) }
                    .padding(vertical = 14.dp, horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isSelected) colorScheme.primary else colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * 二级分类标签栏
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubCategoryTabs(
    subCategories: List<Chapter>,
    subCategoryNames: List<String>,
    selectedId: Int?,
    onSubCategoryClick: (Chapter) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val selectedIndex = subCategories.indexOfFirst { it.id == selectedId }.coerceAtLeast(0)

    SecondaryScrollableTabRow(
        selectedTabIndex = selectedIndex,
        modifier = Modifier.fillMaxWidth(),
        edgePadding = 12.dp,
        containerColor = colorScheme.surface,
        divider = {},
        indicator = {
            Box(
                Modifier
                    .tabIndicatorOffset(selectedIndex, false)
                    .height(3.dp)
                    .background(
                        color = colorScheme.primary,
                        shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp)
                    )
            )
        }
    ) {
        subCategories.forEachIndexed { index, chapter ->
            Tab(
                selected = chapter.id == selectedId,
                onClick = { onSubCategoryClick(chapter) },
                text = {
                    Text(
                        text = subCategoryNames.getOrElse(index) { "" },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = if (chapter.id == selectedId) FontWeight.SemiBold else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                selectedContentColor = colorScheme.primary,
                unselectedContentColor = colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 分类文章列表
 */
@Composable
private fun CategoryArticleList(
    pagingItems: LazyPagingItems<Article>,
    onArticleClick: (Article) -> Unit
) {
    val loadState = pagingItems.loadState

    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = Modifier.fillMaxSize(),
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
