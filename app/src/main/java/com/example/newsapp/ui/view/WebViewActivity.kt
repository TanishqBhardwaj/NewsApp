package com.example.newsapp.ui.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.databinding.ActivityWebViewBinding
import com.example.newsapp.utils.Constants
import com.example.newsapp.utils.WebViewClientImpl

class WebViewActivity: AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()  // to handle back navigation
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpView() {
        setToolBar()
        setWebView()
    }

    private fun setToolBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setWebView() {
        val webView = binding.webView
        webView.webViewClient = WebViewClientImpl()
        val articleUrl = intent.getStringExtra(Constants.ARTICLE_URL)
        if (articleUrl != null) {
            webView.loadUrl(articleUrl)
        }
    }
}