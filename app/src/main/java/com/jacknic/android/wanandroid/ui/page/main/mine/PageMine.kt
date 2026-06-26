package com.jacknic.android.wanandroid.ui.page.main.mine

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.KeyboardArrowRight
import androidx.compose.material.icons.twotone.AccountCircle
import androidx.compose.material.icons.twotone.Brightness4
import androidx.compose.material.icons.twotone.Brightness7
import androidx.compose.material.icons.twotone.BrightnessAuto
import androidx.compose.material.icons.twotone.Create
import androidx.compose.material.icons.twotone.Drafts
import androidx.compose.material.icons.twotone.Face
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material.icons.twotone.Group
import androidx.compose.material.icons.twotone.LocalActivity
import androidx.compose.material.icons.twotone.Notifications
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jacknic.android.wanandroid.core.common.StateResult
import com.jacknic.android.wanandroid.core.ui.R
import com.jacknic.android.wanandroid.ui.page.LocalCollectStateManager
import com.jacknic.android.wanandroid.ui.page.LocalNavCtrl
import com.jacknic.android.wanandroid.ui.page.LocalReadingHistoryManager
import com.jacknic.android.wanandroid.ui.page.Page
import com.jacknic.android.wanandroid.ui.theme.ThemeMode
import com.jacknic.android.wanandroid.ui.theme.themeMode
import com.jacknic.android.wanandroid.ui.theme.useThemeMode

/**
 * 我的页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageMine(vm: MineViewModel = hiltViewModel()) {
    val nav = LocalNavCtrl.current
    val collectStateManager = LocalCollectStateManager.current
    val readingHistoryManager = LocalReadingHistoryManager.current
    val personalInfoState by vm.personalInfo.collectAsStateWithLifecycle()
    val collectIds by collectStateManager.collectIds.collectAsState()
    val readingHistoryList by readingHistoryManager.readingHistoryFlow.collectAsState(initial = emptyList())
    val data = (personalInfoState as? StateResult.Success)?.data
    val userInfo = data?.userInfo
    val coinInfo = data?.coinInfo

    val savedScroll = vm.getScrollState()
    val scrollState = rememberSaveable(saver = ScrollState.Saver) {
        ScrollState(initial = savedScroll.second)
    }

    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.value to scrollState.maxValue }
            .collect { (value, _) ->
                vm.saveScrollState(0, value)
            }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                actions = {
                    IconButton(onClick = {
                        val nextMode = when (themeMode) {
                            ThemeMode.SYSTEM -> ThemeMode.LIGHT
                            ThemeMode.LIGHT -> ThemeMode.DARK
                            ThemeMode.DARK -> ThemeMode.SYSTEM
                        }
                        useThemeMode(nextMode)
                    }) {
                        val names = stringArrayResource(R.array.theme_mode_names)
                        Icon(
                            when (themeMode) {
                                ThemeMode.SYSTEM -> Icons.TwoTone.BrightnessAuto
                                ThemeMode.LIGHT -> Icons.TwoTone.Brightness7
                                ThemeMode.DARK -> Icons.TwoTone.Brightness4
                            },
                            contentDescription = names[themeMode.ordinal],
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.TwoTone.Notifications, "通知")
                    }
                    IconButton(onClick = {
                        nav.navigate(Page.Setting)
                    }) {
                        Icon(Icons.TwoTone.Settings, "设置")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(scrolledContainerColor = MaterialTheme.colorScheme.surface),
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(scrollState),
        ) {
            // 用户信息区域
            UserInfoSection(
                nickname = userInfo?.nickname ?: "未登录",
                level = coinInfo?.level ?: 0,
                coinCount = coinInfo?.coinCount ?: 0,
                rank = coinInfo?.rank ?: "-",
                collectCount = if (collectIds.isNotEmpty()) collectIds.size else (userInfo?.collectIds?.size ?: 0),
                readingCount = readingHistoryList.size,
                onCollectClick = { nav.navigate(Page.Collection) },
                onReadingClick = { nav.navigate(Page.ReadingHistory) },
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 功能卡片
            FeatureCard()

            Spacer(modifier = Modifier.height(12.dp))

            // 创作者中心
            CreatorCenter(onTodoClick = { nav.navigate(Page.Todo) })

            Spacer(modifier = Modifier.height(12.dp))

            // 更多功能
            MoreFeatures(
                onReadingHistory = { nav.navigate(Page.ReadingHistory) },
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * 用户信息区域
 */
@Composable
private fun UserInfoSection(
    nickname: String,
    level: Int,
    coinCount: Int,
    rank: String,
    collectCount: Int,
    readingCount: Int = 0,
    onCollectClick: () -> Unit = {},
    onReadingClick: () -> Unit = {},
) {
    Column(modifier = Modifier.padding(16.dp)) {
        // 头像和用户名
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                Icons.TwoTone.AccountCircle,
                contentDescription = "头像",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        nickname,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        "个人主页",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Icon(
                        Icons.AutoMirrored.TwoTone.KeyboardArrowRight,
                        contentDescription = "",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                // 等级和徽章
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // 等级标签
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier,
                    ) {
                        Text(
                            "Lv.$level",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    // 积分标签
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                    ) {
                        Text(
                            "JY.$coinCount",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    // 排名
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                    ) {
                        Text(
                            "排名 $rank",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 统计数据
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            StatItem(
                label = "阅读",
                value = readingCount.toString(),
                onClick = onReadingClick,
            )
            StatItem(
                label = "收藏",
                value = collectCount.toString(),
                onClick = onCollectClick,
            )
            StatItem("关注", "12")
        }
    }
}

@Composable
private fun StatItem(label: String, value: String, onClick: (() -> Unit)? = null) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .then(if (onClick != null) Modifier.clip(MaterialTheme.shapes.small).clickable { onClick() } else Modifier)
            .padding(horizontal = 12.dp, vertical = 4.dp),
    ) {
        Text(
            value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/**
 * 功能卡片
 */
@Composable
private fun FeatureCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        val tabs = listOf(
            "每日签到" to Icons.TwoTone.Star,
            "幸运转盘" to Icons.TwoTone.LocalActivity,
            "Bug挑战赛" to Icons.TwoTone.Favorite,
            "福利兑换" to Icons.TwoTone.Create,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            tabs.forEach { (label, icon) ->
                Column(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .weight(1f)
                        .clickable { }
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(44.dp),
                    ) {
                        Icon(
                            icon,
                            contentDescription = label,
                            modifier = Modifier
                                .size(44.dp)
                                .padding(10.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(label, fontSize = 12.sp)
                }
            }
        }
    }
}

/**
 * 创作者中心
 */
@Composable
private fun CreatorCenter(onTodoClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column {
            // 标题行
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "创作者中心",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { },
                ) {
                    Text(
                        "进入首页",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Icon(
                        Icons.AutoMirrored.TwoTone.KeyboardArrowRight,
                        contentDescription = "",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            // 功能项
            val creatorItems = listOf(
                CreatorItem("内容数据", Icons.TwoTone.Create),
                CreatorItem("粉丝数据", Icons.TwoTone.Group),
                CreatorItem("创作活动", Icons.TwoTone.LocalActivity),
                CreatorItem("Todo", Icons.TwoTone.Drafts, onTodoClick),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                creatorItems.forEach { item ->
                    Column(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.small)
                            .weight(1f)
                            .clickable { item.onClick?.invoke() }
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            item.icon,
                            contentDescription = item.label,
                            modifier = Modifier.size(28.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(item.label, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

private data class CreatorItem(val label: String, val icon: ImageVector, val onClick: (() -> Unit)? = null)

/**
 * 更多功能
 */
@Composable
private fun MoreFeatures(onReadingHistory: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "更多功能",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )
            }

            val moreItems = listOf(
                MoreFeatureItem("课程中心", Icons.TwoTone.Create),
                MoreFeatureItem("推广中心", Icons.TwoTone.LocalActivity),
                MoreFeatureItem("我的优惠券", Icons.TwoTone.Star),
                MoreFeatureItem("我的圈子", Icons.TwoTone.Group),
                MoreFeatureItem("阅读记录", Icons.TwoTone.Drafts, onReadingHistory),
                MoreFeatureItem("标签管理", Icons.TwoTone.Favorite),
                MoreFeatureItem("我的报名", Icons.TwoTone.Face),
                MoreFeatureItem("意见反馈", Icons.TwoTone.Notifications),
            )

            // 第一行
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                moreItems.take(4).forEach { item ->
                    FeatureGridItem(item, Modifier.weight(1f))
                }
            }
            // 第二行
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                moreItems.drop(4).forEach { item ->
                    FeatureGridItem(item, Modifier.weight(1f))
                }
            }
        }
    }
}

private data class MoreFeatureItem(val label: String, val icon: ImageVector, val onClick: (() -> Unit)? = null)

@Composable
private fun FeatureGridItem(item: MoreFeatureItem, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .clickable { item.onClick?.invoke() }
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            item.icon,
            contentDescription = item.label,
            modifier = Modifier.size(28.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            item.label,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
