package com.jacknic.android.wanandroid.ui.page.browser

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.AndroidView
import com.jacknic.android.wanandroid.ui.page.LocalNavCtrl

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PageBrowser() {

    val nav = LocalNavCtrl.current
    val backStackEntry = nav.previousBackStackEntry
    val savedStateHandle = backStackEntry?.savedStateHandle
    val link = savedStateHandle?.get<String?>("link")
    var webView: WebView? = null
    val titleState = remember { mutableStateOf("") }
    val progress = remember { mutableFloatStateOf(0f) }

    Scaffold(topBar = {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = {
                    if (webView?.canGoBack() == true) {
                        webView?.goBack()
                    } else {
                        nav.navigateUp()
                    }
                }) {
                    Icon(Icons.AutoMirrored.TwoTone.ArrowBack, "")
                }
            },
            actions = {
                IconButton(onClick = { nav.navigateUp() }) {
                    Icon(Icons.TwoTone.Close, "")
                }
            },
            title = {
                Text(
                    titleState.value,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            })
    }) { padding ->
        Box(Modifier.padding(padding)) {
            AndroidView(
                factory = { ctx ->
                    WebView(ctx).also { webView = it }
                }, modifier = Modifier.fillMaxSize(1f)
            ) { web ->
                val settings = web.settings
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                // webView.width = LayoutParams.MATCH_PARENT
                web.webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView,
                        request: WebResourceRequest
                    ): Boolean {
                        val url = request.url.toString()
                        return !url.startsWith("http", true)
                    }
                }
                web.webChromeClient = object : WebChromeClient() {
                    override fun onReceivedTitle(view: WebView?, title: String?) {
                        titleState.value = title.toString()
                    }

                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        progress.floatValue = newProgress / 100f
                    }
                }
                web.loadUrl(link.toString())
            }
            val progressValue = progress.floatValue
            if (progressValue < 1) {
                LinearProgressIndicator(
                    progress = { progressValue },
                    modifier = Modifier.fillMaxWidth(1f),
                )
            }
        }
    }
}