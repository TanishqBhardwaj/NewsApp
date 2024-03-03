package com.example.newsapp.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.model.NewsItem
import com.example.newsapp.model.NewsUiState
import com.example.newsapp.ui.adapter.NewsAdapter
import com.example.newsapp.utils.Constants
import com.example.newsapp.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NewsAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var newsAdapter: NewsAdapter
    private val newsViewModel by viewModels<NewsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingState()
        setSupportActionBar(binding.toolbar)
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
}