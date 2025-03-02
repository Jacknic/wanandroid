package com.jacknic.android.wanandroid.ui.page.main.mine

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.KeyboardArrowRight
import androidx.compose.material.icons.twotone.AccountCircle
import androidx.compose.material.icons.twotone.Face
import androidx.compose.material.icons.twotone.Notifications
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jacknic.android.wanandroid.ui.page.LocalNavCtrl
import com.jacknic.android.wanandroid.ui.page.Page

/**
 * 我的页面
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun PageMine() {
    val nav = LocalNavCtrl.current
    Scaffold(topBar = {
        TopAppBar(
            title = { }, actions = {
                IconButton(onClick = { }) {
                    Icon(Icons.TwoTone.Notifications, "")
                }
                IconButton(onClick = {
                    nav.navigate(Page.Setting)
                }) {
                    Icon(Icons.TwoTone.Settings, "")
                }
            })
    }) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Row {
                Image(Icons.TwoTone.AccountCircle, "", modifier = Modifier.size(100.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Jacknic",
                            modifier = Modifier.weight(1f),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text("个人主页", fontSize = 12.sp)
                        Icon(
                            Icons.AutoMirrored.TwoTone.KeyboardArrowRight,
                            "",
                            modifier = Modifier.size(12.dp)
                        )
                    }
                    Row {
                        IconButton(onClick = { }) {

                        }
                    }
                }
            }
            Card(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(16.dp)
            ) {
                val tabs = arrayOf(
                    "每日签到" to Icons.TwoTone.Face,
                    "幸运转盘" to Icons.TwoTone.Face,
                    "Bug挑战赛" to Icons.TwoTone.Face,
                    "福利兑换" to Icons.TwoTone.Face,
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    tabs.forEach {
                        Column(
                            modifier = Modifier
                                .clickable { }
                                .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(it.second, "")
                            Text(it.first, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}