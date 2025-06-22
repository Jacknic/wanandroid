package com.jacknic.android.wanandroid.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.jacknic.android.wanandroid.ui.theme.WanandroidTheme

/**
 * 搜索栏控件
 *
 * @author Jacknic
 */
@Composable
fun SearchBar(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    val contentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.7f)
    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(0.7f),
                shape = MaterialTheme.shapes.small
            )
            .clip(MaterialTheme.shapes.small)
            .clickable { onClick() }
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Rounded.Search, tint = contentColor, contentDescription = "")
        Text("搜索玩安卓", style = MaterialTheme.typography.titleSmall, color = contentColor)
    }
}

@PreviewScreenSizes
@Preview(showSystemUi = false)
@Composable
fun SearchBarPreview() {
    WanandroidTheme {
        SearchBar(modifier = Modifier.fillMaxWidth())
    }
}