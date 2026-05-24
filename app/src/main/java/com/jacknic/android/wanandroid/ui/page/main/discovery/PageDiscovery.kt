package com.jacknic.android.wanandroid.ui.page.main.discovery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Category
import androidx.compose.material.icons.twotone.Explore
import androidx.compose.material.icons.twotone.Language
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.text.parseAsHtml
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jacknic.android.wanandroid.R
import com.jacknic.android.wanandroid.core.common.getDataOrNull
import com.jacknic.android.wanandroid.core.model.Chapter
import com.jacknic.android.wanandroid.core.model.FriendLink
import com.jacknic.android.wanandroid.core.model.HotKeyword
import com.jacknic.android.wanandroid.ui.page.LocalNavCtrl
import com.jacknic.android.wanandroid.ui.page.Page
import com.jacknic.android.wanandroid.ui.page.openBrowser
import com.jacknic.android.wanandroid.ui.page.search.SEARCH_KEY

/**
 * 发现页
 *
 * @author Jacknic
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageDiscovery(vm: DiscoveryViewModel = hiltViewModel(), onNavigateToHomeCategory: (Int) -> Unit = {}) {
    val nav = LocalNavCtrl.current
    val hotkeyResult by vm.hotkeyResult.collectAsStateWithLifecycle()
    val friendResult by vm.friendResult.collectAsStateWithLifecycle()
    val projectTreeResult by vm.chapterResult.collectAsStateWithLifecycle()

    val hotkeys = hotkeyResult.getDataOrNull() ?: emptyList()
    val friendLinks = friendResult.getDataOrNull() ?: emptyList()
    val projectTrees = projectTreeResult.getDataOrNull() ?: emptyList()

    val savedScroll = vm.getScrollState()
    val listState = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState(
            firstVisibleItemIndex = savedScroll.first,
            firstVisibleItemScrollOffset = savedScroll.second,
        )
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }.collect { (index, offset) ->
            vm.saveScrollState(index, offset)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_discovery)) },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.TwoTone.Explore,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
                actions = {
                    IconButton(onClick = { nav.navigate(Page.Search) }) {
                        Icon(Icons.TwoTone.Search, "搜索")
                    }
                },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(bottom = 16.dp),
        ) {
            if (hotkeys.isNotEmpty()) {
                item {
                    DiscoveryCardSection(
                        title = stringResource(R.string.title_hot_search),
                        icon = Icons.TwoTone.Search,
                    ) {
                        HotkeyFlow(hotkeys = hotkeys) { key ->
                            nav.currentBackStackEntry?.savedStateHandle?.set(SEARCH_KEY, key)
                            nav.navigate(Page.Search)
                        }
                    }
                }
            }

            if (friendLinks.isNotEmpty()) {
                item {
                    DiscoveryCardSection(
                        title = stringResource(R.string.title_useful_sites),
                        icon = Icons.TwoTone.Language,
                    ) {
                        FriendLinkFlow(links = friendLinks) { link ->
                            nav.openBrowser(link.link)
                        }
                    }
                }
            }

            if (projectTrees.isNotEmpty()) {
                item {
                    DiscoveryCardSection(
                        title = stringResource(R.string.title_project_category),
                        icon = Icons.TwoTone.Category,
                    ) {
                        ProjectTreeFlow(trees = projectTrees) { tree ->
                            onNavigateToHomeCategory(tree.id)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DiscoveryCardSection(title: String, icon: ImageVector, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Card(
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(modifier = Modifier.padding(bottom = 12.dp)) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.secondary,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title.parseAsHtml().toString(),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            content()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HotkeyFlow(hotkeys: List<HotKeyword>, onKeyClick: (String) -> Unit) {
    FlowRow(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        hotkeys.forEach { key ->
            SuggestionChip(
                onClick = { onKeyClick(key.name) },
                label = { Text(key.name.parseAsHtml().toString()) },
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FriendLinkFlow(links: List<FriendLink>, onLinkClick: (FriendLink) -> Unit) {
    FlowRow(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        links.forEach { link ->
            AssistChip(
                onClick = { onLinkClick(link) },
                label = { Text(link.name) },
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProjectTreeFlow(trees: List<Chapter>, onTreeClick: (Chapter) -> Unit) {
    FlowRow(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        trees.forEach { tree ->
            AssistChip(
                onClick = { onTreeClick(tree) },
                label = { Text(tree.name.parseAsHtml().toString()) },
            )
        }
    }
}
