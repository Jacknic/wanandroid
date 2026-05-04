package com.jacknic.android.wanandroid.ui.page.main.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jacknic.android.wanandroid.core.common.StateResult
import com.jacknic.android.wanandroid.core.common.toStateResult
import com.jacknic.android.wanandroid.core.domain.WanRepository
import com.jacknic.android.wanandroid.core.model.Article
import com.jacknic.android.wanandroid.core.model.Chapter
import com.jacknic.android.wanandroid.core.model.Tree
import com.jacknic.android.wanandroid.util.PagingListDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 分类页视图数据
 */
@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repo: WanRepository
) : ViewModel() {

    private val _treeResult = MutableStateFlow<StateResult<List<Tree>>>(StateResult.Loading)
    val treeResult = _treeResult.asStateFlow()

    /** 当前选中的一级分类索引 */
    private val _selectedParentIndex = MutableStateFlow(0)
    val selectedParentIndex = _selectedParentIndex.asStateFlow()

    /** 当前选中的二级分类ID */
    private val _selectedChildId = MutableStateFlow<Int?>(null)
    val selectedChildId = _selectedChildId.asStateFlow()

    private val pagingFlows = mutableMapOf<Int, Flow<PagingData<Article>>>()

    init {
        loadTree()
    }

    private fun loadTree() {
        viewModelScope.launch {
            _treeResult.emit(repo.getTree().toStateResult())
        }
    }

    /**
     * 选中一级分类
     */
    fun selectParent(index: Int) {
        val trees = (_treeResult.value as? StateResult.Success)?.data ?: return
        if (index < 0 || index >= trees.size) return
        _selectedParentIndex.value = index
        // 自动选中该一级分类下的第一个子分类
        val children = trees[index].children
        _selectedChildId.value = children.firstOrNull()?.id
    }

    /**
     * 选中二级分类
     */
    fun selectChild(chapterId: Int) {
        _selectedChildId.value = chapterId
    }

    /**
     * 获取指定分类的文章列表流
     */
    fun getArticleListFlow(cid: Int): Flow<PagingData<Article>> {
        return pagingFlows.getOrPut(cid) {
            PagingListDataSource.pager(
                loadAction = { page, pageSize ->
                    repo.getHomeArticleList(page, pageSize, cid.toString())
                }
            ).flow.cachedIn(viewModelScope)
        }
    }

    fun refresh() {
        loadTree()
    }
}
