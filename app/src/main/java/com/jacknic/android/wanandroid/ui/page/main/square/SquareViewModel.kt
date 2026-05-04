package com.jacknic.android.wanandroid.ui.page.main.square

import androidx.lifecycle.ViewModel
import com.jacknic.android.wanandroid.core.domain.WanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 广场页面视图数据
 */
@HiltViewModel
class SquareViewModel @Inject constructor(
    private val repo: WanRepository
) : ViewModel() {

}
