package com.jacknic.android.wanandroid.ui.page.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
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
@OptIn(ExperimentalMaterial3Api::class)
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

    val pagerState = rememberPagerState { categories.size }
    val scope = rememberCoroutineScope()

    val containerColor = MaterialTheme.colorScheme.surfaceContainer
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
                    Spacer(Modifier.size(8.dp))
                    IconButton(onClick = {
                        nav.navigate(Page.Search)
                    }) {
                        Icon(Icons.TwoTone.AccountCircle, "签到")
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(scrolledContainerColor = MaterialTheme.colorScheme.surface)
            )
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize()
        ) {
            if (categories.isNotEmpty()) {
                SecondaryScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    modifier = Modifier.fillMaxWidth(),
                    edgePadding = 8.dp,
                    containerColor = MaterialTheme.colorScheme.surface,
                    divider = {},
                    indicator = {
                        Box(
                            Modifier
                                .tabIndicatorOffset(pagerState.currentPage, true)
                                .height(4.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )
                    }
                ) {
                    categories.forEachIndexed { index, category ->
                        val selected = index == pagerState.currentPage
                        Tab(
                            selected = selected,
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
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            selectedContentColor = MaterialTheme.colorScheme.primary,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                HorizontalDivider(color = containerColor)
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { pageIndex ->
                val category = categories[pageIndex]
                val pagingItems = vm.getArticleListFlow(category.id).collectAsLazyPagingItems()

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    if (pageIndex == 0 && banners.isNotEmpty()) {
                        item {
                            HomeBanner(banners)
                        }
                    }
                    items(pagingItems.itemCount) {
                        val article = pagingItems[it] ?: return@items
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
        }
    }
}
