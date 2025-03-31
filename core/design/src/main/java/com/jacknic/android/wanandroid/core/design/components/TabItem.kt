package com.jacknic.android.wanandroid.core.design.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewScreenSizes

@Composable
fun TabItem(onClick: () -> Unit, selected: Boolean) {
    Tab(
        selected,
        onClick,
        text = {
            Text("控件预览")
        },
        icon = { Icon(Icons.AutoMirrored.TwoTone.List, "") }
    )
}

@PreviewScreenSizes
@Composable
private fun PreviewTabItem() {
    MaterialTheme {
        TabItem({}, false)
    }
}