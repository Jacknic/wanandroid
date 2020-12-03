package com.jacknic.wanandroid.ui.page.user.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.jacknic.wanandroid.databinding.PageLoginBinding
import com.jacknic.wanandroid.ext.hideInput
import com.jacknic.wanandroid.ui.base.AbstractFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginPage : AbstractFragment<PageLoginBinding>() {

    private val vm by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.vm = vm
        vm.loginState.observe(viewLifecycleOwner, Observer { state ->
            bind.btnLogin.isEnabled = !state.loading
            if (state.loading) {
                bind.btnLogin.hideInput()
            }
            state.error?.let {
                Snackbar.make(bind.btnLogin, it.message.toString(), Snackbar.LENGTH_SHORT).show()
            }
        })
    }
}