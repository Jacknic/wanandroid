package com.jacknic.android.wanandroid.core.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TabItem(onClick: () -> Unit, selected: Boolean, text: String) {
    val styleColor =
        if (selected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurfaceVariant.copy(0.5f)
    CompositionLocalProvider(
        LocalContentColor provides styleColor
    ) {
        val cornerShape = RoundedCornerShape(50)
        Text(
            text,
            color = styleColor,
            fontSize = 12.sp,
            modifier = Modifier
                .clip(cornerShape)
                .clickable { onClick() }
                .background(styleColor.copy(0.1f), cornerShape)
                .padding(horizontal = 16.dp, vertical = 4.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewScreenSizes
@Composable
fun PreviewTabItem() {
    MaterialTheme {
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        SecondaryScrollableTabRow(
            selectedTabIndex,
            edgePadding = 16.dp,
            containerColor = Color.Transparent,
            divider = {
                HorizontalDivider(thickness = 8.dp, color = Color.Transparent)
            },
            indicator = {}) {
            for (i in 0 until 15) {
                Row {
                    if (i != 0) {
                        Spacer(Modifier.width(8.dp))
                    }
                    TabItem({ selectedTabIndex = i }, i == selectedTabIndex, "控件预览 $i")
                }
            }
        }
    }
}