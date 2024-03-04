package com.example.newsapp.ui.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.model.NewsItem
import com.example.newsapp.model.NewsUiState
import com.example.newsapp.ui.adapter.NewsAdapter
import com.example.newsapp.utils.Constants
import com.example.newsapp.viewmodel.NewsViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NewsAdapter.OnItemClickListener {

    private val tag = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private lateinit var newsAdapter: NewsAdapter
    private val newsViewModel by viewModels<NewsViewModel>()

    // Request permission launcher
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK can post notifications.
        } else {
            Toast.makeText(this, Constants.NOTIFICATION_PERMISSION_ERROR, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
        getNewsData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return when (id) {
            R.id.action_sort_latest -> {
                getNewsData(true)
                true
            }
            R.id.action_sort_oldest -> {
                getNewsData(false)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onItemClick(newsItem: NewsItem) {
        /**
         * Opening web view in separate activity (Fragment is preferred),
         * just to have simple back stack
         */
        Intent(this, WebViewActivity()::class.java).also {
            it.putExtra(Constants.ARTICLE_URL, newsItem.url)
            startActivity(it)
        }
    }

    private fun setUpView() {
        loadingState()
        setSupportActionBar(binding.toolbar)
        requestPermissions()
        fetchingFCMToken()
    }

    private fun getNewsData(latestInd: Boolean = true) {
        newsViewModel.getNewsData(latestInd)
        // Launching coroutine for UI operations
        lifecycleScope.launch(Dispatchers.Main) {
            newsViewModel.newsListState.collect {
                when (it) {
                    is NewsUiState.Loading -> {
                        loadingState()
                    }

                    is NewsUiState.Success -> {
                        setRecyclerView(it.data)
                    }

                    is NewsUiState.Error -> {
                        loadedState()
                        Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun setRecyclerView(newsList: List<NewsItem>) {
        newsAdapter = NewsAdapter(newsList, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = newsAdapter
        loadedState()
    }

    private fun loadingState() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }

    private fun loadedState() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK can post notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun fetchingFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(tag, Constants.FETCHING_FCM_TOKEN_FAILED, task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            //TODO This token should not be logged (for security reasons), but doing it intentionally for testing for specific device
            Log.d(tag, token)
        })
    }
}