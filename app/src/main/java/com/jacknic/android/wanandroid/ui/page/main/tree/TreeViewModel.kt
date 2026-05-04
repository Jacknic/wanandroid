package com.jacknic.android.wanandroid.ui.page.main.tree

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jacknic.android.wanandroid.core.common.StateResult
import com.jacknic.android.wanandroid.core.common.toStateResult
import com.jacknic.android.wanandroid.core.domain.WanRepository
import com.jacknic.android.wanandroid.core.model.Article
import com.jacknic.android.wanandroid.core.model.Tree
import com.jacknic.android.wanandroid.util.PagingListDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 体系页视图数据
 */
@HiltViewModel
class TreeViewModel @Inject constructor(
    private val repo: WanRepository
) : ViewModel() {

    private val _treeResult = MutableStateFlow<StateResult<List<Tree>>>(StateResult.Loading)
    val treeResult = _treeResult.asStateFlow()

    /** 当前搜索关键字 */
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    /** 展开的一级分类索引集合 */
    private val _expandedIndices = MutableStateFlow<Set<Int>>(emptySet())
    val expandedIndices = _expandedIndices.asStateFlow()

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
     * 切换一级分类的展开/折叠状态
     */
    fun toggleExpand(index: Int) {
        _expandedIndices.value = _expandedIndices.value.toMutableSet().apply {
            if (contains(index)) remove(index) else add(index)
        }
    }

    /**
     * 选中二级分类
     */
    fun selectChild(chapterId: Int) {
        _selectedChildId.value = chapterId
    }

    /**
     * 更新搜索关键字
     */
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    /**
     * 根据搜索关键字筛选体系数据
     */
    fun filterTrees(trees: List<Tree>): List<Tree> {
        val query = _searchQuery.value.trim()
        if (query.isEmpty()) return trees
        return trees.mapNotNull { tree ->
            val nameMatch = tree.name.contains(query, ignoreCase = true)
            val matchedChildren = tree.children.filter {
                it.name.contains(query, ignoreCase = true)
            }
            if (nameMatch) {
                tree // 一级分类名匹配，保留全部子分类
            } else if (matchedChildren.isNotEmpty()) {
                tree.copy(children = matchedChildren)
            } else {
                null
            }
        }
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
