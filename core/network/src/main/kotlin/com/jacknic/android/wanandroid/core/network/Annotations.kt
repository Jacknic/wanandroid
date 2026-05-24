@file:Suppress("MatchingDeclarationName")

package com.jacknic.android.wanandroid.core.network

/**
 * 分页标记注释，用于标注分页接口的页码起始值和分页大小范围
 *
 * @property pageStart 起始页码，0或1
 * @property pageSizeMin 最小分页大小，默认1
 * @property pageSizeMax 最大分页大小，默认40
 */
@Retention(AnnotationRetention.SOURCE)
annotation class PageNotice(
    val pageStart: Int = 0,
    val pageSizeMin: Int = 1,
    val pageSizeMax: Int = 40
)
