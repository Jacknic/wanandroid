package com.jacknic.android.wanandroid.ui.page.todo

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.CheckCircle
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.icons.twotone.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jacknic.android.wanandroid.core.common.StateResult
import com.jacknic.android.wanandroid.core.model.Todo
import com.jacknic.android.wanandroid.core.network.isUnauthorized
import com.jacknic.android.wanandroid.ui.page.LocalNavCtrl
import com.jacknic.android.wanandroid.ui.page.Page
import com.jacknic.android.wanandroid.ui.page.navTop
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Todo管理页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageTodo(vm: TodoViewModel = hiltViewModel()) {
    val nav = LocalNavCtrl.current
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val todoListState by vm.todoList.collectAsStateWithLifecycle()
    var isRefreshing by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var editingTodo by remember { mutableStateOf<Todo?>(null) }
    var todoToDelete by remember { mutableStateOf<Todo?>(null) }

    val lazyListState = rememberLazyListState()

    // 滚动到底部自动加载更多
    LaunchedEffect(lazyListState) {
        snapshotFlow {
            val lastVisibleIndex = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = lazyListState.layoutInfo.totalItemsCount
            lastVisibleIndex to totalItems
        }.collect { (lastVisible, total) ->
            if (lastVisible >= total - 2 && total > 0) {
                vm.loadMore()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todo") },
                navigationIcon = {
                    IconButton(onClick = { nav.navigateUp() }) {
                        Icon(Icons.AutoMirrored.TwoTone.ArrowBack, "返回")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.TwoTone.Add, "新增")
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
        ) {
            // 筛选条
            FilterBar(vm = vm)

            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = {
                    isRefreshing = true
                    vm.refresh()
                    isRefreshing = false
                },
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
            ) {
                when (val state = todoListState) {
                    is StateResult.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is StateResult.Error -> {
                        val isUnauthorized = state.exception?.isUnauthorized() ?: false
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    if (isUnauthorized) "未登录" else "加载失败",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                if (isUnauthorized) {
                                    TextButton(onClick = { nav.navTop(Page.Login, Page.Main) }) {
                                        Text("去登录")
                                    }
                                } else {
                                    TextButton(onClick = { vm.refresh() }) {
                                        Text("重试")
                                    }
                                }
                            }
                        }
                    }

                    is StateResult.Success -> {
                        val todos = state.data.datas
                        val total = state.data.total
                        if (todos.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    "暂无待办事项",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        } else {
                            LazyColumn(state = lazyListState) {
                                stickyHeader {
                                    Surface(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            "共 $total 项",
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(
                                                horizontal = 16.dp,
                                                vertical = 8.dp,
                                            ),
                                        )
                                    }
                                }
                                items(items = todos, key = { it.id }) { todo ->
                                    TodoItem(
                                        todo = todo,
                                        onToggleStatus = { vm.toggleTodoStatus(todo) },
                                        onEdit = { editingTodo = todo },
                                        onDelete = { todoToDelete = todo },
                                    )
                                }
                                if (!state.data.over) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    null -> {}
                }
            }
        }
    }

    // 新增对话框
    if (showAddDialog) {
        TodoEditDialog(
            title = "新增待办",
            initialTitle = "",
            initialContent = "",
            initialDate = todayStr(),
            onDismiss = { showAddDialog = false },
            onConfirm = { title, content, date ->
                vm.addTodo(title, content, date) { success ->
                    if (success) {
                        showAddDialog = false
                    } else {
                        Toast.makeText(context, "添加失败", Toast.LENGTH_SHORT).show()
                    }
                }
            },
        )
    }

    // 编辑对话框
    editingTodo?.let { todo ->
        TodoEditDialog(
            title = "编辑待办",
            initialTitle = todo.title,
            initialContent = todo.content,
            initialDate = todo.date.ifBlank { todayStr() },
            onDismiss = { editingTodo = null },
            onConfirm = { title, content, date ->
                vm.updateTodo(
                    id = todo.id,
                    title = title,
                    content = content,
                    date = date,
                    status = todo.status,
                ) { success ->
                    if (success) {
                        editingTodo = null
                    } else {
                        Toast.makeText(context, "更新失败", Toast.LENGTH_SHORT).show()
                    }
                }
            },
        )
    }

    // 删除确认对话框
    todoToDelete?.let { todo ->
        AlertDialog(
            onDismissRequest = { todoToDelete = null },
            title = { Text("删除待办") },
            text = {
                Text(
                    todo.title.take(50).let { if (it.length < todo.title.length) "$it…" else it },
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    vm.deleteTodo(todo.id)
                    todoToDelete = null
                }) {
                    Text("删除", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { todoToDelete = null }) {
                    Text("取消")
                }
            },
        )
    }
}

@Composable
private fun FilterBar(vm: TodoViewModel) {
    val currentFilter by remember { mutableStateOf(vm.statusFilter) }
    var selectedFilter by remember(currentFilter) { mutableStateOf(vm.statusFilter) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FilterChip(
            selected = selectedFilter == null,
            onClick = {
                selectedFilter = null
                vm.setFilter(null)
            },
            label = { Text("全部") },
        )
        FilterChip(
            selected = selectedFilter == 0,
            onClick = {
                selectedFilter = 0
                vm.setFilter(0)
            },
            label = { Text("未完成") },
        )
        FilterChip(
            selected = selectedFilter == 1,
            onClick = {
                selectedFilter = 1
                vm.setFilter(1)
            },
            label = { Text("已完成") },
        )
    }
}

@Composable
private fun TodoItem(
    todo: Todo,
    onToggleStatus: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    val isDone = todo.status == 1
    val titleColor by animateColorAsState(
        targetValue = if (isDone) MaterialTheme.colorScheme.onSurfaceVariant
        else MaterialTheme.colorScheme.onSurface,
        label = "titleColor",
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clip(MaterialTheme.shapes.medium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 状态切换按钮
                IconButton(
                    onClick = onToggleStatus,
                    modifier = Modifier.size(32.dp),
                ) {
                    Icon(
                        imageVector = if (isDone) Icons.TwoTone.CheckCircle else Icons.TwoTone.RadioButtonUnchecked,
                        contentDescription = if (isDone) "已完成" else "未完成",
                        tint = if (isDone) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp),
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                // 标题
                Text(
                    text = todo.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = titleColor,
                    textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                // 编辑按钮
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(28.dp),
                ) {
                    Icon(
                        Icons.TwoTone.Edit,
                        contentDescription = "编辑",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                // 删除按钮
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(28.dp),
                ) {
                    Icon(
                        Icons.TwoTone.Delete,
                        contentDescription = "删除",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.error,
                    )
                }
            }
            // 内容
            if (todo.content.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = todo.content,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontStyle = if (isDone) FontStyle.Italic else FontStyle.Normal,
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 40.dp),
                )
            }
            // 日期
            if (todo.dateStr.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = todo.dateStr,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(start = 40.dp),
                )
            }
        }
    }
}

@Composable
private fun TodoEditDialog(
    title: String,
    initialTitle: String,
    initialContent: String,
    initialDate: String,
    onDismiss: () -> Unit,
    onConfirm: (title: String, content: String, date: String) -> Unit,
) {
    var todoTitle by remember { mutableStateOf(initialTitle) }
    var todoContent by remember { mutableStateOf(initialContent) }
    var todoDate by remember { mutableStateOf(initialDate) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = todoTitle,
                    onValueChange = { todoTitle = it },
                    label = { Text("标题") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = todoContent,
                    onValueChange = { todoContent = it },
                    label = { Text("详情") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = todoDate,
                    onValueChange = { todoDate = it },
                    label = { Text("日期 (yyyy-MM-dd)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(todoTitle, todoContent, todoDate) },
                enabled = todoTitle.isNotBlank(),
            ) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        },
    )
}

private fun todayStr(): String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
