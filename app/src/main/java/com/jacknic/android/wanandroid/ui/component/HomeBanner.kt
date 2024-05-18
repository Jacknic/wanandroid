package com.jacknic.android.wanandroid.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jacknic.android.wanandroid.core.model.Banner

/**
 * 首页 banner 组件
 */
@ExperimentalFoundationApi
@Composable
fun HomeBanner(bannerList: List<Banner>) {
    val pagerState = rememberPagerState()
    HorizontalPager(
        pageCount = bannerList.size,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(6.dp)),
        state = pagerState,
    ) {
        val banner = bannerList[it]
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            model = banner.imagePath,
            contentDescription = null,
        )
    }
    // Text("当前页面: ${pagerState.currentPage}")
}