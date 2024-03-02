package com.example.newsapp.utils

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

// WebViewClient to handle page loading and other events
class WebViewClientImpl: WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        // To load in web view and not other browser apps
        return false
    }
}