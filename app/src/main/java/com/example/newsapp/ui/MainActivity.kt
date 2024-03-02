package com.example.newsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.model.NewsItem
import com.example.newsapp.model.NewsUiState
import com.example.newsapp.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var newsAdapter: NewsAdapter
    private val newsViewModel by viewModels<NewsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingState()
        getNewsData()
    }

    private fun getNewsData() {
        newsViewModel.getNewsData()
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
        newsAdapter = NewsAdapter(newsList)
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