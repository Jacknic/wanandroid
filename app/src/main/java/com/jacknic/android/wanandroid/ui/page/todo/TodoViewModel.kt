package com.jacknic.android.wanandroid.ui.page.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacknic.android.wanandroid.core.common.StateResult
import com.jacknic.android.wanandroid.core.common.toStateResult
import com.jacknic.android.wanandroid.core.common.withLoading
import com.jacknic.android.wanandroid.core.domain.WanRepository
import com.jacknic.android.wanandroid.core.model.Paging
import com.jacknic.android.wanandroid.core.model.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TodoViewModel @Inject constructor(private val repo: WanRepository) : ViewModel() {

    private val _todoList = MutableStateFlow<StateResult<Paging<Todo>>?>(null)
    val todoList = _todoList.asStateFlow()

    private var currentPage = 1
    private var hasMore = true

    /** 筛选状态: null=全部, 0=未完成, 1=已完成 */
    var statusFilter: Int? = null

    init {
        loadTodoList()
    }

    private fun loadTodoList() {
        viewModelScope.launch {
            _todoList.withLoading {
                repo.getTodoList(page = 1, status = statusFilter).toStateResult()
            }
            val paging = (_todoList.value as? StateResult.Success)?.data
            currentPage = paging?.curPage ?: 1
            hasMore = !(paging?.over ?: true)
        }
    }

    fun loadMore() {
        if (!hasMore) return
        val currentData = (_todoList.value as? StateResult.Success)?.data ?: return
        viewModelScope.launch {
            try {
                val result = repo.getTodoList(page = currentPage, status = statusFilter)
                result.onSuccess { paging ->
                    _todoList.update { prevState ->
                        val prevPaging = (prevState as? StateResult.Success)?.data
                        if (prevPaging != null) {
                            val existingIds = prevPaging.datas.map { it.id }.toSet()
                            val newTodos = paging.datas.filter { it.id !in existingIds }
                            StateResult.Success(
                                prevPaging.copy(
                                    datas = prevPaging.datas + newTodos,
                                    curPage = paging.curPage,
                                    over = paging.over,
                                ),
                            )
                        } else {
                            StateResult.Success(paging)
                        }
                    }
                    currentPage = paging.curPage
                    hasMore = !paging.over
                }
            } catch (_: Exception) {
                // 加载更多失败，保持现有数据
            }
        }
    }

    fun addTodo(title: String, content: String, date: String, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            try {
                val result = repo.addTodo(title = title, content = content, date = date)
                result.onSuccess {
                    refresh()
                    onResult(true)
                }
                result.onFailure { onResult(false) }
            } catch (_: Exception) {
                onResult(false)
            }
        }
    }

    fun updateTodo(id: Int, title: String, content: String, date: String, status: Int, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            try {
                val result = repo.updateTodo(id = id, title = title, content = content, date = date, status = status)
                result.onSuccess {
                    refresh()
                    onResult(true)
                }
                result.onFailure { onResult(false) }
            } catch (_: Exception) {
                onResult(false)
            }
        }
    }

    fun deleteTodo(id: Int) {
        viewModelScope.launch {
            try {
                val result = repo.deleteTodo(id)
                result.onSuccess {
                    _todoList.update { prevState ->
                        val prevPaging = (prevState as? StateResult.Success)?.data ?: return@update prevState
                        StateResult.Success(
                            prevPaging.copy(
                                datas = prevPaging.datas.filter { it.id != id },
                                total = (prevPaging.total - 1).coerceAtLeast(0),
                            ),
                        )
                    }
                }
            } catch (_: Exception) {
                // 删除失败，保持现有数据
            }
        }
    }

    fun toggleTodoStatus(todo: Todo) {
        val newStatus = if (todo.status == 0) 1 else 0
        viewModelScope.launch {
            try {
                val result = repo.doneTodo(id = todo.id, status = newStatus)
                result.onSuccess {
                    _todoList.update { prevState ->
                        val prevPaging = (prevState as? StateResult.Success)?.data ?: return@update prevState
                        StateResult.Success(
                            prevPaging.copy(
                                datas = prevPaging.datas.map {
                                    if (it.id == todo.id) it.copy(status = newStatus) else it
                                },
                            ),
                        )
                    }
                }
            } catch (_: Exception) {
                // 状态切换失败
            }
        }
    }

    fun setFilter(status: Int?) {
        statusFilter = status
        refresh()
    }

    fun refresh() {
        currentPage = 1
        hasMore = true
        loadTodoList()
    }
}
