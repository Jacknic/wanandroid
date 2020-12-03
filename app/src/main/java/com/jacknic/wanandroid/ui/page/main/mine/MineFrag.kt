package com.jacknic.wanandroid.ui.page.main.mine

import android.view.MenuItem
import com.jacknic.wanandroid.R
import com.jacknic.wanandroid.databinding.FragMineBinding
import com.jacknic.wanandroid.ext.toPage
import com.jacknic.wanandroid.ui.base.BaseFragment

/**
 * @author Jacknic
 */
class MineFrag : BaseFragment<FragMineBinding>() {

    override val layoutResId = R.layout.frag_mine
    override val menuResId = R.menu.menu_settings

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_settings) {
            navCtrl.toPage(R.id.settingsPage)
        }
        return super.onOptionsItemSelected(item)
    }
}