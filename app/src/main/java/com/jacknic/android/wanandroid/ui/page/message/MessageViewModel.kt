package com.jacknic.android.wanandroid.ui.page.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacknic.android.wanandroid.BuildConfig
import com.jacknic.android.wanandroid.core.common.StateResult
import com.jacknic.android.wanandroid.core.common.TLog
import com.jacknic.android.wanandroid.core.common.toStateResult
import com.jacknic.android.wanandroid.core.common.withLoading
import com.jacknic.android.wanandroid.core.domain.WanRepository
import com.jacknic.android.wanandroid.core.model.Message
import com.jacknic.android.wanandroid.core.model.Paging
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 消息分类
 */
enum class MessageTab(val labelResId: Int) {
    /** 全部（已读列表 + 未读数量） */
    ALL(com.jacknic.android.wanandroid.core.ui.R.string.message_tab_all),

    /** 未读 */
    UNREAD(com.jacknic.android.wanandroid.core.ui.R.string.message_tab_unread),

    /** 已读 */
    READ(com.jacknic.android.wanandroid.core.ui.R.string.message_tab_read),
}

@HiltViewModel
class MessageViewModel @Inject constructor(private val repo: WanRepository) : ViewModel() {

    private val log = TLog.create("MessageViewModel", BuildConfig.DEBUG)

    private val _messageList = MutableStateFlow<StateResult<Paging<Message>>?>(null)
    val messageList = _messageList.asStateFlow()

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount = _unreadCount.asStateFlow()

    /** 当前选中的分类 Tab（StateFlow 驱动，确保 UI 响应式更新） */
    private val _currentTab = MutableStateFlow(MessageTab.ALL)
    val currentTab = _currentTab.asStateFlow()

    /** 是否正在加载更多 */
    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore = _isLoadingMore.asStateFlow()

    private var currentPage = 1
    private var hasMore = true

    init {
        loadUnreadCount()
        loadMessageList()
    }

    /**
     * 加载未读消息数量
     */
    private fun loadUnreadCount() {
        viewModelScope.launch {
            repo.getMessageLgCountUnread()
                .onSuccess { count -> _unreadCount.value = count }
                .onFailure { e -> log.tag().e("加载未读数量失败: ${e.message}") }
        }
    }

    /**
     * 加载消息列表
     *
     * 根据 API 设计，访问未读消息列表会将所有消息标记为已读。
     * 因此 ALL / READ Tab 使用已读列表，UNREAD Tab 单独加载未读列表。
     */
    private fun loadMessageList() {
        viewModelScope.launch {
            _messageList.withLoading {
                when (_currentTab.value) {
                    MessageTab.ALL, MessageTab.READ -> {
                        repo.getMessageLgReadedList(page = 1).toStateResult()
                    }

                    MessageTab.UNREAD -> {
                        repo.getMessageLgUnreadedList(page = 1).toStateResult()
                    }
                }
            }
            val paging = (_messageList.value as? StateResult.Success)?.data
            currentPage = paging?.curPage ?: 1
            hasMore = !(paging?.over ?: true)
        }
    }

    /**
     * 加载更多
     */
    fun loadMore() {
        if (!hasMore || _isLoadingMore.value) return
        val currentData = (_messageList.value as? StateResult.Success)?.data ?: return
        _isLoadingMore.value = true
        viewModelScope.launch {
            try {
                val result = when (_currentTab.value) {
                    MessageTab.ALL, MessageTab.READ -> repo.getMessageLgReadedList(page = currentPage)
                    MessageTab.UNREAD -> repo.getMessageLgUnreadedList(page = currentPage)
                }
                result.onSuccess { paging ->
                    _messageList.update { prevState ->
                        val prevPaging = (prevState as? StateResult.Success)?.data
                        if (prevPaging != null) {
                            val existingIds = prevPaging.datas.map { it.id }.toSet()
                            val newMessages = paging.datas.filter { it.id !in existingIds }
                            StateResult.Success(
                                prevPaging.copy(
                                    datas = prevPaging.datas + newMessages,
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
                }.onFailure { e ->
                    log.tag().e("加载更多失败: ${e.message}")
                }
            } catch (e: Exception) {
                log.tag().e("加载更多异常: ${e.message}")
            } finally {
                _isLoadingMore.value = false
            }
        }
    }

    /**
     * 切换分类 Tab
     */
    fun switchTab(tab: MessageTab) {
        if (tab == _currentTab.value) return
        _currentTab.value = tab
        currentPage = 1
        hasMore = true
        loadMessageList()
    }

    /**
     * 标记全部已读
     *
     * 访问未读消息列表接口会使所有消息变为已读，
     * 然后刷新已读列表并清零未读数量。
     */
    fun markAllRead(onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            try {
                val result = repo.getMessageLgUnreadedList(page = 1)
                result.onSuccess {
                    _unreadCount.value = 0
                    _currentTab.value = MessageTab.READ
                    currentPage = 1
                    hasMore = true
                    loadMessageList()
                    onResult(true)
                }.onFailure { e ->
                    log.tag().e("标记全部已读失败: ${e.message}")
                    onResult(false)
                }
            } catch (e: Exception) {
                log.tag().e("标记全部已读异常: ${e.message}")
                onResult(false)
            }
        }
    }

    /**
     * 删除单条消息（本地移除）
     *
     * 玩安卓 API 未提供消息删除接口，此处进行本地移除并更新 UI。
     */
    fun deleteMessage(messageId: Int) {
        viewModelScope.launch {
            _messageList.update { prevState ->
                val prevPaging = (prevState as? StateResult.Success)?.data ?: return@update prevState
                StateResult.Success(
                    prevPaging.copy(
                        datas = prevPaging.datas.filter { it.id != messageId },
                        total = (prevPaging.total - 1).coerceAtLeast(0),
                    ),
                )
            }
        }
    }

    /**
     * 刷新
     */
    fun refresh() {
        currentPage = 1
        hasMore = true
        loadUnreadCount()
        loadMessageList()
    }
}
