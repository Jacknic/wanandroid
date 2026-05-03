package com.jacknic.android.wanandroid.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
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
 * 文章列表项 - 掘金风格卡片
 */
@Composable
fun ArticleListItem(
    article: Article,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // 封面图 + 标题 + 描述
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // 标题
                    Text(
                        text = article.title.parseAsHtml().toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // 描述
                    if (article.desc.isNotBlank()) {
                        Text(
                            text = article.desc,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 18.sp
                        )
                    }
                }

                // 封面图
                if (article.envelopePic.isNotBlank()) {
                    Spacer(modifier = Modifier.width(10.dp))
                    AsyncImage(
                        modifier = Modifier
                            .size(100.dp, 80.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop,
                        model = article.envelopePic,
                        contentDescription = null,
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 作者信息 + 操作按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 作者信息
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = article.author.ifBlank { article.shareUser },
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (article.niceDate.isNotBlank()) {
                        Text(
                            text = " · ${article.niceDate}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
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
            zan = 666,
            niceDate = "2小时前"
        )
        ArticleListItem(article)
    }
}
