package com.jacknic.android.wanandroid.core.network

/**
 * 分页标记注释
 *
 * @property pageStart 起始页，一般是0或1
 * @property pageSizeMin 最小分页数量 1
 * @property pageSizeMax 最大分页数量, 一般为 40
 */
@Retention(AnnotationRetention.SOURCE)
annotation class PageNotice(
    val pageStart: Int = 0,
    val pageSizeMin: Int = 1,
    val pageSizeMax: Int = 40
)