package com.jacknic.android.wanandroid.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AccountCircle
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.MoreVert
import androidx.compose.material.icons.twotone.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.parseAsHtml
import coil.compose.AsyncImage
import com.jacknic.android.wanandroid.core.model.Article
import com.jacknic.android.wanandroid.ui.theme.WanandroidTheme

/**
 * 文章列表项
 */
@Composable
fun ArticleListItem(article: Article, onClick: () -> Unit = {}) {
    Column(modifier = Modifier
        .clickable { onClick() }
        .background(MaterialTheme.colorScheme.surface)
        .padding(8.dp)) {
        val html = article.title.parseAsHtml()
        Text(html.toString(), fontSize = 18.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Spacer(Modifier.size(6.dp))
        Row {
            val defaultUrl = "https://developer.android.google.cn/images/home/droid.svg"
            val imgUrl = article.envelopePic.ifEmpty { defaultUrl }
            Column(Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Image(Icons.TwoTone.AccountCircle, "")
                    Text(article.author, fontSize = 12.sp)
                }
                Text(article.descMd)
            }
            AsyncImage(
                modifier = Modifier
                    .width(108.dp)
                    .height(72.dp),
                contentScale = ContentScale.Crop,
                model = imgUrl,
                contentDescription = null,
            )

        }
        val colorFilter = LocalContentColor.current.copy(0.5f)
        val bgColor = MaterialTheme.colorScheme.onBackground.copy(0.05f)
        CompositionLocalProvider(LocalContentColor provides colorFilter) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Image(Icons.TwoTone.ThumbUp, "", colorFilter = ColorFilter.tint(colorFilter))
                    Text(article.zan.toString())
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Image(Icons.TwoTone.Info, "", colorFilter = ColorFilter.tint(colorFilter))
                    Text(article.zan.toString())
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Android",

                        modifier = Modifier
                            .background(bgColor, RoundedCornerShape(6.dp))
                            .padding(3.dp),
                        fontSize = 12.sp
                    )
                    Icon(Icons.TwoTone.MoreVert, "")
                }
            }
        }

    }
}

@Preview(device = Devices.PHONE)
@Composable
private fun Preview() {
    WanandroidTheme {
        val article = Article(
            title = "Android 项目实战——手把手教你实现一款本地音乐播放器",
            author = "FaceBlack",
            desc = "简介这是一个怎么样的工具，简介这是一个怎么样的工具，简介这是一个怎么样的工具，简介这是一个怎么样的工具，",
            zan = 666
        )
        ArticleListItem(article)
    }
}