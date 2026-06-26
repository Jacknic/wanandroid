package com.jacknic.android.wanandroid.ui.page.message

import android.content.Intent
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material.icons.automirrored.twotone.OpenInNew
import androidx.compose.material.icons.twotone.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import com.jacknic.android.wanandroid.core.ui.R
import com.jacknic.android.wanandroid.ui.page.LocalNavCtrl

/**
 * 消息详情页面
 *
 * 展示消息完整内容，支持 HTML 富文本解析与链接点击跳转。
 * 数据通过 SavedStateHandle 从消息列表页传递。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageMessageDetail(
    title: String,
    content: String,
    link: String,
    fullLink: String,
    niceDate: String,
    tag: String,
    category: Int,
    fromName: String,
) {
    val nav = LocalNavCtrl.current
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { nav.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.TwoTone.ArrowBack,
                            stringResource(R.string.action_back),
                        )
                    }
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            // 消息头部信息
            MessageDetailHeader(
                title = title,
                fromName = fromName,
                niceDate = niceDate,
                tag = tag,
                category = category,
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 消息内容（富文本）
            MessageContent(
                content = content,
                contentPadding = PaddingValues(horizontal = 16.dp),
            )

            // 链接按钮
            if (link.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                ) {
                    Spacer(Modifier.weight(1.0f))
                    Button(
                        onClick = {
                            val url = fullLink.ifEmpty { link }
                            context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
                        },
                    ) {
                        Icon(
                            Icons.AutoMirrored.TwoTone.OpenInNew,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(stringResource(R.string.message_open_link))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * 消息详情头部
 */
@Composable
private fun MessageDetailHeader(title: String, fromName: String, niceDate: String, tag: String, category: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        // 标题
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 发送者信息
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.TwoTone.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = fromName.ifEmpty { "系统通知" },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = niceDate,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 标签
        if (tag.isNotEmpty()) {
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.secondaryContainer,
            ) {
                Text(
                    text = tag,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                )
            }
        }
    }
}

/**
 * 消息富文本内容
 *
 * 使用 AndroidView + TextView + HtmlCompat 解析 HTML 标签和链接。
 */
@Composable
private fun MessageContent(content: String, contentPadding: PaddingValues) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(contentPadding),
        factory = {
            TextView(it).apply {
                movementMethod = LinkMovementMethod.getInstance()
                textSize = 15f
                val spanned = HtmlCompat.fromHtml(
                    content,
                    HtmlCompat.FROM_HTML_MODE_COMPACT,
                )
                text = spanned
            }
        },
        update = { textView ->
            val spanned = HtmlCompat.fromHtml(
                content,
                HtmlCompat.FROM_HTML_MODE_COMPACT,
            )
            textView.text = spanned
        },
    )
}
