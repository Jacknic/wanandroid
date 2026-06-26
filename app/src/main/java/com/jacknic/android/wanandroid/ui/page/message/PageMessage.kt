package com.jacknic.android.wanandroid.ui.page.message

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material.icons.automirrored.twotone.Article
import androidx.compose.material.icons.twotone.CloudOff
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.DoneAll
import androidx.compose.material.icons.twotone.MarkChatUnread
import androidx.compose.material.icons.twotone.MarkunreadMailbox
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jacknic.android.wanandroid.core.common.StateResult
import com.jacknic.android.wanandroid.core.model.Message
import com.jacknic.android.wanandroid.core.network.isUnauthorized
import com.jacknic.android.wanandroid.core.ui.R
import com.jacknic.android.wanandroid.ui.page.LocalNavCtrl
import com.jacknic.android.wanandroid.ui.page.Page
import com.jacknic.android.wanandroid.ui.page.navTop

private const val ANIM_DURATION = 300

/**
 * 消息中心页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageMessage(vm: MessageViewModel = hiltViewModel()) {
    val nav = LocalNavCtrl.current
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val messageListState by vm.messageList.collectAsStateWithLifecycle()
    val unreadCount by vm.unreadCount.collectAsStateWithLifecycle()
    val currentTab by vm.currentTab.collectAsStateWithLifecycle()
    val isLoadingMore by vm.isLoadingMore.collectAsStateWithLifecycle()
    var isRefreshing by remember { mutableStateOf(false) }
    var messageToDelete by remember { mutableStateOf<Message?>(null) }

    val lazyListState = rememberLazyListState()

    // 滚动到底部自动加载更多
    LaunchedEffect(lazyListState) {
        snapshotFlow {
            val lastVisible = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val total = lazyListState.layoutInfo.totalItemsCount
            lastVisible to total
        }.collect { (lastVisible, total) ->
            if (lastVisible >= total - 2 && total > 0) {
                vm.loadMore()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.page_message)) },
                navigationIcon = {
                    IconButton(onClick = { nav.navigateUp() }) {
                        Icon(Icons.AutoMirrored.TwoTone.ArrowBack, stringResource(R.string.action_back))
                    }
                },
                actions = {
                    IconButton(onClick = {
                        vm.markAllRead { success ->
                            val msg = if (success) {
                                context.getString(R.string.message_mark_all_read_success)
                            } else {
                                context.getString(R.string.error_unknown)
                            }
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(Icons.TwoTone.DoneAll, stringResource(R.string.message_mark_all_read))
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
        ) {
            // 分类筛选 Tab
            MessageTabRow(
                currentTab = currentTab,
                unreadCount = unreadCount,
                onTabSelected = { vm.switchTab(it) },
            )

            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = {
                    isRefreshing = true
                    vm.refresh()
                    isRefreshing = false
                },
                modifier = Modifier.fillMaxSize(),
            ) {
                AnimatedContent(
                    targetState = messageListState,
                    transitionSpec = {
                        fadeIn(tween(ANIM_DURATION)) togetherWith fadeOut(tween(ANIM_DURATION))
                    },
                    label = "messageStateTransition",
                ) { state ->
                    when (state) {
                        is StateResult.Loading -> LoadingState()

                        is StateResult.Error -> ErrorState(
                            isUnauthorized = state.exception?.isUnauthorized() ?: false,
                            onRetry = { vm.refresh() },
                            onLogin = { nav.navTop(Page.Login, Page.Main) },
                        )

                        is StateResult.Success -> {
                            val messages = state.data.datas
                            if (messages.isEmpty()) {
                                EmptyState()
                            } else {
                                MessageList(
                                    messages = messages,
                                    lazyListState = lazyListState,
                                    isUnreadTab = currentTab == MessageTab.UNREAD,
                                    hasMore = !state.data.over,
                                    isLoadingMore = isLoadingMore,
                                    onItemClick = { message ->
                                        val handle = nav.currentBackStackEntry?.savedStateHandle
                                        handle?.set(KEY_MESSAGE_ID, message.id)
                                        handle?.set(KEY_MESSAGE_TITLE, message.title)
                                        handle?.set(KEY_MESSAGE_CONTENT, message.message)
                                        handle?.set(KEY_MESSAGE_LINK, message.link)
                                        handle?.set(KEY_MESSAGE_FULL_LINK, message.fullLink)
                                        handle?.set(KEY_MESSAGE_NICE_DATE, message.niceDate)
                                        handle?.set(KEY_MESSAGE_TAG, message.tag)
                                        handle?.set(KEY_MESSAGE_CATEGORY, message.category)
                                        handle?.set(KEY_MESSAGE_FROM_NAME, message.fromUser)
                                        nav.navigate(Page.MessageDetail)
                                    },
                                    onDelete = { messageToDelete = it },
                                )
                            }
                        }

                        null -> LoadingState()
                    }
                }
            }
        }
    }

    // 删除确认对话框
    messageToDelete?.let { message ->
        AlertDialog(
            onDismissRequest = { messageToDelete = null },
            title = { Text(stringResource(R.string.message_delete_title)) },
            text = {
                Text(
                    message.title.take(50)
                        .let { if (it.length < message.title.length) "$it…" else it },
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    vm.deleteMessage(message.id)
                    messageToDelete = null
                    Toast.makeText(
                        context,
                        context.getString(R.string.message_delete_success),
                        Toast.LENGTH_SHORT,
                    ).show()
                }) {
                    Text(
                        stringResource(R.string.action_confirm),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { messageToDelete = null }) {
                    Text(stringResource(R.string.action_cancel))
                }
            },
        )
    }
}

/**
 * 消息列表
 */
@Composable
private fun MessageList(
    messages: List<Message>,
    lazyListState: androidx.compose.foundation.lazy.LazyListState,
    isUnreadTab: Boolean,
    hasMore: Boolean,
    isLoadingMore: Boolean,
    onItemClick: (Message) -> Unit,
    onDelete: (Message) -> Unit,
) {
    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = messages,
            key = { it.id },
        ) { message ->
            MessageItem(
                message = message,
                isUnread = isUnreadTab,
                onClick = { onItemClick(message) },
                onDelete = { onDelete(message) },
            )
        }
        if (hasMore) {
            item(key = "footer_loading") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    if (isLoadingMore) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    }
                }
            }
        }
    }
}

/**
 * 加载中状态
 */
@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

/**
 * 空状态
 */
@Composable
private fun EmptyState() {
    StatePlaceholder(
        icon = Icons.TwoTone.MarkunreadMailbox,
        title = stringResource(R.string.message_empty_title),
        description = stringResource(R.string.message_empty_desc),
    )
}

/**
 * 错误状态
 */
@Composable
private fun ErrorState(isUnauthorized: Boolean, onRetry: () -> Unit, onLogin: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.TwoTone.CloudOff,
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (isUnauthorized) {
                    stringResource(R.string.error_not_logged_in)
                } else {
                    stringResource(R.string.message_error_title)
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (isUnauthorized) {
                    stringResource(R.string.error_not_logged_in)
                } else {
                    stringResource(R.string.message_error_desc)
                },
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(20.dp))
            if (isUnauthorized) {
                Button(onClick = onLogin) {
                    Text(stringResource(R.string.action_login))
                }
            } else {
                Button(onClick = onRetry) {
                    Text(stringResource(R.string.action_retry))
                }
            }
        }
    }
}

/**
 * 通用空状态占位组件
 */
@Composable
private fun StatePlaceholder(icon: ImageVector, title: String, description: String? = null) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (description != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

/**
 * 消息分类 Tab 行
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MessageTabRow(currentTab: MessageTab, unreadCount: Int, onTabSelected: (MessageTab) -> Unit) {
    val tabs = MessageTab.entries
    val selectedIndex = tabs.indexOf(currentTab)

    PrimaryTabRow(selectedTabIndex = selectedIndex) {
        tabs.forEach { tab ->
            val label = stringResource(tab.labelResId)
            val selected = tab == currentTab
            Tab(
                selected = selected,
                onClick = { onTabSelected(tab) },
                text = {
                    if (tab == MessageTab.UNREAD && unreadCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge { Text(unreadCount.toString()) }
                            },
                        ) {
                            Text(
                                label,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            )
                        }
                    } else {
                        Text(
                            label,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        )
                    }
                },
            )
        }
    }
}

/**
 * 单条消息卡片
 */
@Composable
private fun MessageItem(message: Message, isUnread: Boolean, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnread) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            } else {
                MaterialTheme.colorScheme.surface
            },
        ),
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            // 未读标识圆点
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(
                        if (isUnread) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            Color.Transparent
                        },
                    )
                    .padding(top = 4.dp),
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                // 标题
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isUnread) {
                            Icons.TwoTone.MarkChatUnread
                        } else {
                            Icons.AutoMirrored.TwoTone.Article
                        },
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (isUnread) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = message.title,
                        fontSize = 15.sp,
                        fontWeight = if (isUnread) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 消息内容预览
                Text(
                    text = message.message,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(6.dp))

                // 发送者和时间
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val fromName = message.fromUser
                    if (fromName.isNotEmpty()) {
                        Text(
                            text = stringResource(R.string.message_from, fromName),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false),
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = message.niceDate,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            // 删除按钮
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    imageVector = Icons.TwoTone.Delete,
                    contentDescription = stringResource(R.string.action_delete),
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

internal const val KEY_MESSAGE_ID = "key_message_id"
internal const val KEY_MESSAGE_TITLE = "key_message_title"
internal const val KEY_MESSAGE_CONTENT = "key_message_content"
internal const val KEY_MESSAGE_LINK = "key_message_link"
internal const val KEY_MESSAGE_FULL_LINK = "key_message_full_link"
internal const val KEY_MESSAGE_NICE_DATE = "key_message_nice_date"
internal const val KEY_MESSAGE_TAG = "key_message_tag"
internal const val KEY_MESSAGE_CATEGORY = "key_message_category"
internal const val KEY_MESSAGE_FROM_NAME = "key_message_from_name"
