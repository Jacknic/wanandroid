package com.jacknic.wanandroid.device

import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * 通知管理
 *
 * @author Jacknic
 */
object NotificationUtils {

    const val CHANNEL_ID_UPDATE = "update"
    const val CHANNEL_ID_DOWNLOAD = "download"
    const val CHANNEL_ID_PUSH = "push"

    /**
     * 通道信息
     *
     * 通道ID => 优先级
     */
    private val channelMap = mapOf(
        CHANNEL_ID_UPDATE to 1,
        CHANNEL_ID_DOWNLOAD to 1,
        CHANNEL_ID_PUSH to 1
    )

    /**
     * 注册消息通道信息
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun register(context: Context) {
        val manager = NotificationManagerCompat.from(context)
        val nameList = context.resources.getStringArray(R.array.channel_name_list)
        val channelList = mutableListOf<NotificationChannel>()
        channelMap.keys.forEachIndexed { index, id ->
            val importance = channelMap.getValue(id)
            val notificationChannel = NotificationChannel(id, nameList[index], importance)
            channelList.add(notificationChannel)
        }
        manager.createNotificationChannels(channelList)
    }

    fun buildUpdateNotify(context: Context) {
        val manager = NotificationManagerCompat.from(context)
        NotificationCompat.Extender {
            it.setContentTitle("检测更新")
            it.setProgress(100, 1, true)
        }
    }
}