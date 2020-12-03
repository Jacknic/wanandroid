package com.jacknic.wanandroid.ui.page.browser

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import com.jacknic.wanandroid.R
import com.jacknic.wanandroid.databinding.PageBrowserBinding
import com.jacknic.wanandroid.ext.openLink
import com.jacknic.wanandroid.ext.toast
import com.jacknic.wanandroid.ui.base.BasePage

/**
 * 内置浏览器页面
 *
 * @author Jacknic
 */
class BrowserPage : BasePage<PageBrowserBinding>() {

    override val layoutResId = R.layout.page_browser
    override val menuResId = R.menu.browser_actions
    private lateinit var backPressedCallback: OnBackPressedCallback
    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null
    private val codeChoiceFile = 10000
    private val supportSchemes = listOf("http", "https", "file")

    @SuppressLint("RestrictedApi", "RequiresFeature")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: BrowserPageArgs = try {
            BrowserPageArgs.fromBundle(arguments!!)
        } catch (e: Exception) {
            requireContext().toast(R.string.browser_argument_error)
            null
        } ?: return
        setupWebView()
        backPressedCallback = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() = bind.webView.goBack()
        }
        val url = args.url
        bind.webView.loadUrl(url)
        activity?.apply {
            this.title = args.title
            onBackPressedDispatcher.addCallback(this@BrowserPage, backPressedCallback)
        }
    }

    /**
     * 设置webView控件
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        bind.webView.settings.apply {
            javaScriptEnabled = true
            displayZoomControls = false
            javaScriptCanOpenWindowsAutomatically = true
            domStorageEnabled = true
            useWideViewPort = true
            allowFileAccess = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            blockNetworkImage = false
            loadsImagesAutomatically = true
            builtInZoomControls = true
            setSupportZoom(true)
        }

        // 内置浏览控件设置
        bind.webView.apply {
            webViewClient = object : WebViewClient() {

                override fun shouldOverrideUrlLoading(
                    webView: WebView,
                    request: WebResourceRequest
                ): Boolean {
                    val uri = request.url
                    if (uri.scheme !in supportSchemes) {
                        context.openLink(uri.toString())
                        return true
                    }
                    return super.shouldOverrideUrlLoading(webView, request)
                }
            }
            webChromeClient = object : WebChromeClient() {
                override fun onReceivedTitle(view: WebView, title: String) {
                    if (title != view.url && isResumed) {
                        activity?.title = title
                    }
                }

                override fun onProgressChanged(view: WebView, newProgress: Int) {
                    val visible = newProgress < 100
                    if (visible) {
                        bind.progressBar.visibility = View.VISIBLE
                        bind.progressBar.progress = newProgress
                    } else {
                        backPressedCallback.isEnabled = canGoBack()
                        bind.progressBar.visibility = View.GONE
                    }
                }

                override fun onShowFileChooser(
                    webView: WebView?,
                    filePathCallback: ValueCallback<Array<Uri>>?,
                    fileChooserParams: FileChooserParams?
                ): Boolean {
                    openFileChooser()
                    mFilePathCallback = filePathCallback
                    return true
                }
            }
            setDownloadListener { url, _, _, _, _ ->
                AlertDialog.Builder(requireContext())
                    .setTitle(R.string.tips_file_download)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok) { dialog, _ ->
                        dialog.dismiss()
                        requireContext().openLink(
                            url,
                            getString(R.string.action_title_file_download)
                        )
                    }.show()
            }
        }
    }

    /**
     * 选择本地文件
     */
    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        val actionTitle = getString(R.string.action_title_file_upload)
        startActivityForResult(Intent.createChooser(intent, actionTitle), codeChoiceFile)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == codeChoiceFile) {
            if (data != null && resultCode == Activity.RESULT_OK) {
                data.apply {
                    val uri = getData()
                    val resultData: Array<Uri>? = if (uri == null) null else arrayOf(uri)
                    mFilePathCallback?.onReceiveValue(resultData)
                }
            } else {
                mFilePathCallback?.onReceiveValue(null)
                mFilePathCallback = null
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_open_with -> {
                val url = bind.webView.url
                if (url.startsWith("file")) {
                    requireContext().toast(R.string.inner_page_tips)
                } else {
                    val actionTitle = getString(R.string.action_title_choice_browser)
                    requireContext().openLink(url, actionTitle)
                }
                return true
            }
            R.id.action_refresh -> {
                bind.webView.reload()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        bind.webView.stopLoading()
        super.onDestroyView()
    }
}