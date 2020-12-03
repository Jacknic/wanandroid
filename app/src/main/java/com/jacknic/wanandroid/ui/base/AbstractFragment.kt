package com.jacknic.wanandroid.ui.base

import android.os.Bundle
import android.view.*
import androidx.annotation.MenuRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jacknic.wanandroid.ext.generateBinding
import java.lang.reflect.ParameterizedType

/**
 * Fragment 抽象类
 *
 * @author Jacknic
 */
abstract class AbstractFragment<T : ViewDataBinding> : Fragment() {

    /**
     * 无效菜单资源
     **/
    private val invalidMenuRes = 0
    protected val navCtrl by lazy { findNavController() }
    protected lateinit var bind: T

    /**
     * 菜单资源
     */
    @MenuRes
    open val menuResId = invalidMenuRes

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedState: Bundle?
    ): View? {
        if (menuResId != invalidMenuRes) {
            setHasOptionsMenu(true)
        }
        val type = javaClass.genericSuperclass as ParameterizedType
        val clazz = type.actualTypeArguments[0] as Class<T>
        bind = generateBinding(inflater, clazz, container)
        return bind.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (menuResId != invalidMenuRes) {
            inflater.inflate(menuResId, menu)
        }
    }

}