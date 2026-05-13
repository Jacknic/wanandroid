package com.jacknic.android.wanandroid.ui.page.main.discovery

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacknic.android.wanandroid.core.common.StateResult
import com.jacknic.android.wanandroid.core.common.toStateResult
import com.jacknic.android.wanandroid.core.domain.WanRepository
import com.jacknic.android.wanandroid.core.model.Chapter
import com.jacknic.android.wanandroid.core.model.FriendLink
import com.jacknic.android.wanandroid.core.model.HotKeyword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 发现页视图数据
 *
 * @author Jacknic
 */
@HiltViewModel
class DiscoveryViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repo: WanRepository
) : ViewModel() {

    private val _hotkeyResult = MutableStateFlow<StateResult<List<HotKeyword>>>(StateResult.Loading)
    val hotkeyResult = _hotkeyResult.asStateFlow()

    private val _friendResult = MutableStateFlow<StateResult<List<FriendLink>>>(StateResult.Loading)
    val friendResult = _friendResult.asStateFlow()

    private val _chapterResult = MutableStateFlow<StateResult<List<Chapter>>>(StateResult.Loading)
    val chapterResult = _chapterResult.asStateFlow()

    init {
        if (_hotkeyResult.value is StateResult.Loading) {
            refresh()
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _hotkeyResult.emit(repo.getHotkey().toStateResult())
            _friendResult.emit(repo.getFriend().toStateResult())
            _chapterResult.emit(repo.getProjectTree().toStateResult())
        }
    }

    // ==================== 状态保存与恢复 ====================

    fun saveScrollState(firstVisibleItemIndex: Int, firstVisibleItemScrollOffset: Int) {
        savedStateHandle[KEY_SCROLL_INDEX] = firstVisibleItemIndex
        savedStateHandle[KEY_SCROLL_OFFSET] = firstVisibleItemScrollOffset
    }

    fun getScrollState(): Pair<Int, Int> {
        val index = savedStateHandle[KEY_SCROLL_INDEX] ?: 0
        val offset = savedStateHandle[KEY_SCROLL_OFFSET] ?: 0
        return index to offset
    }

    companion object {
        private const val KEY_SCROLL_INDEX = "discovery_scroll_index"
        private const val KEY_SCROLL_OFFSET = "discovery_scroll_offset"
    }
}
