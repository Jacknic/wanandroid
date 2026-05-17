package com.jacknic.android.wanandroid.ui.page.main.mine

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacknic.android.wanandroid.core.common.StateResult
import com.jacknic.android.wanandroid.core.common.toStateResult
import com.jacknic.android.wanandroid.core.common.withLoading
import com.jacknic.android.wanandroid.core.domain.WanRepository
import com.jacknic.android.wanandroid.core.model.PersonalInfo
import com.jacknic.android.wanandroid.ui.component.CollectStateManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MineViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repo: WanRepository,
    private val collectStateManager: CollectStateManager
) : ViewModel() {

    private val _personalInfo = MutableStateFlow<StateResult<PersonalInfo>?>(null)
    val personalInfo = _personalInfo.asStateFlow()

    init {
        if (_personalInfo.value == null) {
            loadUserInfo()
        }
    }

    private fun loadUserInfo() {
        viewModelScope.launch {
            _personalInfo.withLoading {
                repo.getUserLgUserinfo().toStateResult().also { result ->
                    if (result is StateResult.Success) {
                        collectStateManager.initFromUserInfo(result.data.userInfo.collectIds)
                    }
                }
            }
        }
    }

    fun refresh() = loadUserInfo()

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
        private const val KEY_SCROLL_INDEX = "mine_scroll_index"
        private const val KEY_SCROLL_OFFSET = "mine_scroll_offset"
    }
}
