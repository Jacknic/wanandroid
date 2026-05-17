package com.jacknic.android.wanandroid.ui.page.search

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Clear
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.History
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.jacknic.android.wanandroid.R
import com.jacknic.android.wanandroid.core.common.getDataOrNull
import com.jacknic.android.wanandroid.ui.component.ArticleListItem
import com.jacknic.android.wanandroid.ui.component.CollectResult
import com.jacknic.android.wanandroid.ui.page.LocalCollectStateManager
import com.jacknic.android.wanandroid.ui.page.LocalNavCtrl
import com.jacknic.android.wanandroid.ui.page.Page
import com.jacknic.android.wanandroid.ui.page.main.discovery.DiscoveryCardSection
import com.jacknic.android.wanandroid.ui.page.main.discovery.HotkeyFlow
import com.jacknic.android.wanandroid.ui.page.navTop
import com.jacknic.android.wanandroid.ui.page.openBrowser
import kotlinx.coroutines.launch

/**
 * 搜索页
 *
 * @author Jacknic
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageSearch(
    vm: SearchViewModel = hiltViewModel()
) {
    val nav = LocalNavCtrl.current
    val context = LocalContext.current
    val collectStateManager = LocalCollectStateManager.current
    val collectIds by collectStateManager.collectIds.collectAsState()
    val collectInitialized by collectStateManager.isInitialized.collectAsState()
    val scope = rememberCoroutineScope()
    var showLoginDialog by remember { mutableStateOf(false) }
    val pagingItems = vm.searchResultFlow.collectAsLazyPagingItems()
    val searchKey by vm.searchKey.collectAsState()
    val hotkeyResult by vm.hotkeyResult.collectAsState()
    val searchHistory by vm.searchHistory.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    // 监听返回事件：有搜索内容时先清空，否则返回上一页
    BackHandler(enabled = inputText.isNotEmpty()) {
        inputText = ""
        vm.clearSearch()
    }

    // 处理从其他页面传入的搜索关键词
    LaunchedEffect(Unit) {
        nav.previousBackStackEntry?.savedStateHandle?.remove<String>(SEARCH_KEY)?.let {
            if (it.isNotEmpty()) {
                vm.setSearchKey(it)
            }
        }
    }

    // 同步输入框文本
    LaunchedEffect(searchKey) {
        if (searchKey.isNotEmpty()) {
            inputText = searchKey
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = inputText,
                        onValueChange = {
                            inputText = it
                            if (it.isEmpty()) {
                                vm.clearSearch()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("搜索文章关键词") },
                        suffix = {
                            if (inputText.isNotEmpty()) {
                                IconButton(onClick = {
                                    inputText = ""
                                    vm.clearSearch()
                                }) {
                                    Icon(Icons.TwoTone.Clear, null)
                                }
                            }
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                if (inputText.isNotEmpty()) {
                                    vm.search(inputText)
                                    keyboardController?.hide()
                                }
                            }) { Icon(Icons.TwoTone.Search, null) }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            if (inputText.isNotEmpty()) {
                                vm.search(inputText)
                                keyboardController?.hide()
                            }
                        }),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (inputText.isNotEmpty()) {
                            inputText = ""
                            vm.clearSearch()
                        } else {
                            nav.popBackStack()
                        }
                    }) {
                        Icon(Icons.AutoMirrored.TwoTone.ArrowBack, "返回")
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            val hotkeys = hotkeyResult.getDataOrNull() ?: emptyList()
            if (searchKey.isEmpty()) {
                if (searchHistory.isNotEmpty()) {
                    item {
                        SearchHistorySection(
                            history = searchHistory.toList(),
                            onItemClick = {
                                vm.search(it)
                                keyboardController?.hide()
                            },
                            onDeleteClick = { vm.removeHistory(it) },
                            onClearAllClick = { vm.clearHistory() }
                        )
                    }
                }

                if (hotkeys.isNotEmpty()) {
                    item {
                        DiscoveryCardSection(
                            title = stringResource(R.string.title_hot_search),
                            icon = Icons.TwoTone.Search
                        ) {
                            HotkeyFlow(hotkeys = hotkeys) { key ->
                                vm.search(key)
                                keyboardController?.hide()
                            }
                        }
                    }
                }
            }

            items(pagingItems.itemCount) {
                val article = pagingItems[it] ?: return@items
                Spacer(modifier = Modifier.size(8.dp))
                val isCollected = if (collectInitialized) collectIds.contains(article.id) else article.collect
                ArticleListItem(
                    article = article,
                    isCollected = isCollected,
                    onCollectClick = {
                        scope.launch {
                            when (val result = collectStateManager.toggleCollect(article.id, isCollected)) {
                                is CollectResult.NotLoggedIn -> showLoginDialog = true
                                is CollectResult.Error -> Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                                is CollectResult.Success -> {}
                            }
                        }
                    }
                ) {
                    nav.openBrowser(article.link)
                }
            }

            if (searchKey.isNotEmpty()) {
                val state = pagingItems.loadState
                val modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                when {
                    state.refresh is LoadState.Loading || state.prepend is LoadState.Loading -> {
                        item {
                            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    state.refresh is LoadState.Error -> {
                        item {
                            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                                Button(onClick = { pagingItems.refresh() }) {
                                    Text("加载错误,重新加载")
                                }
                            }
                        }
                    }

                    state.append is LoadState.Error -> {
                        item {
                            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                                Button(onClick = { pagingItems.retry() }) {
                                    Text("加载错误,点击重试")
                                }
                            }
                        }
                    }
                }
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

/**
 * 搜索历史
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchHistorySection(
    history: List<String>,
    onItemClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
    onClearAllClick: () -> Unit
) {
    DiscoveryCardSection(
        title = "搜索历史",
        icon = Icons.TwoTone.History
    ) {
        Column {
            FlowRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                history.forEach { item ->
                    InputChip(
                        selected = false,
                        onClick = { onItemClick(item) },
                        label = { Text(item) },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.TwoTone.Clear,
                                contentDescription = "删除",
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable { onDeleteClick(item) }
                            )
                        }
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = onClearAllClick,
                    modifier = Modifier.height(36.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                ) {
                    Icon(Icons.TwoTone.Delete, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("清空历史", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}
