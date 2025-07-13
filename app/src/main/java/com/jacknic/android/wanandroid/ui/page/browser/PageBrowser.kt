package com.jacknic.android.wanandroid.ui.page.browser

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.os.bundleOf
import com.jacknic.android.wanandroid.ui.page.LocalNavCtrl

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PageBrowser() {

    val nav = LocalNavCtrl.current
    val backStackEntry = nav.previousBackStackEntry
    val savedStateHandle = backStackEntry?.savedStateHandle
    val link = savedStateHandle?.get<String?>("link")
    var titleState by remember { mutableStateOf("") }
    var progress by remember { mutableFloatStateOf(0f) }
    var launchUrl by rememberSaveable { mutableStateOf<String?>(null) }
    val saveBundle = rememberSaveable { bundleOf() }

    Scaffold(topBar = {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = {
                    nav.navigateUp()
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
                    titleState, maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            }
        )
    }) { padding ->
        Box(Modifier.padding(padding)) {
            val webView = rememberWebViewWithState(
                onTitleChange = { titleState = it ?: "" },
                onLoadChange = { progress = it })
            AndroidView(
                factory = { webView },
                modifier = Modifier.fillMaxSize(1f),
                onRelease = {
                    it.saveState(saveBundle)
                    launchUrl = it.url
                    it.stopLoading()
                },
                onReset = {
                    it.stopLoading()
                    it.loadUrl("about:blank")
                },
                update = {}
            )
            LaunchedEffect(Unit) {
                if (saveBundle.isEmpty) {
                    webView.loadUrl(launchUrl ?: link.toString())
                } else {
                    webView.restoreState(saveBundle)
                }
            }
            val progressValue = progress
            if (progressValue < 1) {
                LinearProgressIndicator(
                    progress = { progressValue },
                    modifier = Modifier.fillMaxWidth(1f),
                )
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun rememberWebViewWithState(
    onTitleChange: (String?) -> Unit = {},
    onLoadChange: (Float) -> Unit = {},
): WebView {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView, request: WebResourceRequest
                ): Boolean {
                    val url = request.url.toString()
                    return !url.startsWith("http", true)
                }
            }
            webChromeClient = object : WebChromeClient() {
                override fun onReceivedTitle(view: WebView, title: String?) {
                    if (view.url != title) {
                        onTitleChange(title)
                    }
                }

                override fun onProgressChanged(view: WebView, newProgress: Int) {
                    onLoadChange(newProgress / 100f)
                }
            }
        }
    }

    BackHandler(enabled = webView.canGoBack()) {
        webView.goBack()
    }

    return webView
}