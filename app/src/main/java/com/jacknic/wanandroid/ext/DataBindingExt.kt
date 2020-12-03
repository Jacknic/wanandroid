package com.jacknic.wanandroid.ext

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/// 视图绑定拓展工具

/**
 * 反射生成绑定对象
 */
@Suppress("UNCHECKED_CAST")
fun <T : ViewDataBinding> generateBinding(
    inflater: LayoutInflater,
    clazz: Class<T>,
    container: ViewGroup? = null
): T {
    return if (container == null) {
        val method = clazz.getMethod("inflate", LayoutInflater::class.java)
        method.invoke(null, inflater) as T
    } else {
        val method = clazz.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        method.invoke(null, inflater, container, false) as T
    }
}

/**
 * 获取绑定对象
 */
fun <T : ViewDataBinding> View?.getBinding(parent: ViewGroup, @LayoutRes layoutRes: Int): T {
    val binding = this.findBinding<T>()
    return binding
        ?: DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutRes, parent, false)
}

/**
 * 查找绑定对象
 */
fun <T : ViewDataBinding> View?.findBinding() = this?.let { DataBindingUtil.findBinding<T>(it) }
