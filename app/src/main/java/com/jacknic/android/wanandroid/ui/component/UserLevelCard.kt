package com.jacknic.android.wanandroid.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jacknic.android.wanandroid.core.ui.R

@Composable
fun UserLevelCard(bgRes: Int, frontRes: Int, subLabel: String, state: String) {
    Box(
        modifier = Modifier
            .aspectRatio(329 / 166f, true)
    ) {
        Image(
            painterResource(bgRes),
            "",
            modifier = Modifier.fillMaxSize(),
        )
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painterResource(frontRes),
                "",
                modifier = Modifier.height(24.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(subLabel, fontSize = 12.sp)
            Spacer(modifier = Modifier.weight(1f))
            Text(state, fontSize = 12.sp)
        }
    }
}

@PreviewLightDark
@PreviewScreenSizes
@Composable
fun PreviewUserLevelCard() {
    val resMap = mapOf(
        R.drawable.bg_user_level_card_1 to R.drawable.user_level_font_1,
        R.drawable.bg_user_level_card_2 to R.drawable.user_level_font_2,
        R.drawable.bg_user_level_card_3 to R.drawable.user_level_font_3,
        R.drawable.bg_user_level_card_4 to R.drawable.user_level_font_4,
        R.drawable.bg_user_level_card_5 to R.drawable.user_level_font_5,
        R.drawable.bg_user_level_card_6 to R.drawable.user_level_font_6,
        R.drawable.bg_user_level_card_7 to R.drawable.user_level_font_7,
        R.drawable.bg_user_level_card_8 to R.drawable.user_level_font_8,
    )
    LazyVerticalGrid(
        GridCells.Adaptive(200.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        resMap.forEach { res ->
            item {
                UserLevelCard(
                    res.key,
                    res.value,
                    "达成日期 2025-03-06",
                    "已达成该等级"
                )
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun PreviewUserLevelCardPager() {
    val resMap = listOf(
        R.drawable.bg_user_level_card_1 to R.drawable.user_level_font_1,
        R.drawable.bg_user_level_card_2 to R.drawable.user_level_font_2,
        R.drawable.bg_user_level_card_3 to R.drawable.user_level_font_3,
        R.drawable.bg_user_level_card_4 to R.drawable.user_level_font_4,
        R.drawable.bg_user_level_card_5 to R.drawable.user_level_font_5,
        R.drawable.bg_user_level_card_6 to R.drawable.user_level_font_6,
        R.drawable.bg_user_level_card_7 to R.drawable.user_level_font_7,
        R.drawable.bg_user_level_card_8 to R.drawable.user_level_font_8,
    )
    HorizontalPager(
        rememberPagerState { resMap.size },
        modifier = Modifier.fillMaxWidth(),
        pageSize = PageSize.Fixed(329.dp),
        contentPadding = PaddingValues(16.dp),
        pageSpacing = 16.dp,
        snapPosition = SnapPosition.Center
    ) {
        Card(modifier = Modifier.size(329.dp, 166.dp)) {
            val res = resMap[it]
            UserLevelCard(
                res.first,
                res.second,
                "达成日期 2025-03-06",
                "已达成该等级"
            )
        }
    }
}