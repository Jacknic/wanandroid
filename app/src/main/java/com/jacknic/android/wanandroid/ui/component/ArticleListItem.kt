package com.jacknic.android.wanandroid.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material.icons.twotone.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.core.text.parseAsHtml
import coil.compose.AsyncImage
import com.jacknic.android.wanandroid.core.model.Article
import com.jacknic.android.wanandroid.ui.theme.WanandroidTheme

/**
 * 链接类型枚举
 */
enum class LinkType(val domains: List<String>, val label: String, val color: Color) {
    JUEJIN(listOf("juejin.cn", "juejin.im"), "掘金", Color(0xFF0066FF)),
    WECHAT(listOf("mp.weixin.qq.com"), "微信", Color(0xFF07C160)),
    WANANDROID(listOf("wanandroid.com"), "玩安卓", Color(0xFF276692)),
    CSDN(listOf("csdn.net"), "CSDN", Color(0xFFFA7040)),
    JIANSHU(listOf("jianshu.com"), "简书", Color(0xFFE67E22)),
    ZHIHU(listOf("zhihu.com", "zhuanlan.zhihu.com"), "知乎", Color(0xFF0066FF)),
    GITHUB(listOf("github.com"), "GitHub", Color(0xFF181717)),
    ANDROID_DEVELOPER(listOf("developer.android.com"), "Android", Color(0xFF3DDC84)),
    OTHER(listOf(""), "其他", Color(0xFF868686)),
}

/**
 * 根据链接获取链接类型
 */
private fun getLinkType(link: String): LinkType? {
    if (link.isBlank()) return LinkType.OTHER
    return try {
        val host = link.toUri().host ?: return null
        LinkType.entries.find { linkType ->
            linkType.domains.any { domain -> host.contains(domain) }
        }
    } catch (_: Exception) {
        LinkType.OTHER
    }
}

/**
 * 文章列表项 - 掘金风格卡片
 */
@Composable
fun ArticleListItem(
    article: Article,
    modifier: Modifier = Modifier,
    isCollected: Boolean = article.collect,
    onCollectClick: (() -> Unit)? = null,
    onClick: () -> Unit = {}
) {
    // 预解析 HTML 标题，避免在测量过程中进行重度计算
    val displayTitle = remember(article.title) {
        article.title.parseAsHtml().toString()
    }
    // 获取链接类型
    val linkType = remember(article.link) { getLinkType(article.link) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(12.dp))
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
                        text = displayTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 22.sp,
                        modifier = Modifier.heightIn(min = 44.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // 描述
                    Text(
                        text = article.desc,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 18.sp,
                        modifier = Modifier.heightIn(min = 36.dp)
                    )
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 作者信息
                Text(
                    text = article.author.ifBlank { article.shareUser } + " " + article.niceDate,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                // 链接类型徽章
                linkType?.let { type ->
                    LinkTypeBadge(
                        text = type.label,
                        color = type.color
                    )
                }

                // 收藏按钮
                if (onCollectClick != null) {
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(
                        onClick = onCollectClick,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (isCollected) Icons.TwoTone.Favorite else Icons.TwoTone.FavoriteBorder,
                            contentDescription = if (isCollected) "取消收藏" else "收藏",
                            tint = if (isCollected) Color(0xFFE53935) else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * 链接类型徽章
 */
@Composable
fun LinkTypeBadge(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 6.dp)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            color = color,
            fontWeight = FontWeight.Medium
        )
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
            niceDate = "2小时前",
            link = "https://juejin.cn/post/123456789"
        )
        ArticleListItem(article)
    }
}
