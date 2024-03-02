package com.example.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.newsapp.network.NewsRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.NewsItem
import com.example.newsapp.model.NewsResponse
import com.example.newsapp.model.NewsUiState
import com.example.newsapp.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class NewsViewModel @Inject constructor(private val newsRequest: NewsRequest): ViewModel() {

    private val _newsListState: MutableStateFlow<NewsUiState> = MutableStateFlow(NewsUiState.Loading)
    val newsListState get() = _newsListState.asStateFlow()

    fun getNewsData() {
        /**
         * 1. This scope gets automatically destroyed, when the view model destroys
         * 2. Launching coroutine for IO operation
         */
        viewModelScope.launch(Dispatchers.IO) {
            val newsResponse = newsRequest.makeGetRequest(Constants.BASE_URL)
            if (newsResponse != null) {
                _newsListState.update {
                    NewsUiState.Success(getComputedArticles(newsResponse))
                }
            } else {
                _newsListState.update {
                    NewsUiState.Error(Constants.SOMETHING_WENT_WRONG)
                }
            }
        }
    }

    // To get computed article list (with corrected url)
    private fun getComputedArticles(newsResponse: NewsResponse): List<NewsItem> {
        val newsList = mutableListOf<NewsItem>()
        for (newsItem in newsResponse.articles) {
            newsList.add(
                NewsItem(
                    newsItem.source,
                    newsItem.author,
                    newsItem.title,
                    newsItem.description,
                    ensureHttpsUrl(newsItem.url ?: ""),
                    ensureHttpsUrl(newsItem.urlToImage ?: ""),
                    newsItem.publishedAt,
                    newsItem.content
                )
            )
        }
        return newsList
    }

    private fun ensureHttpsUrl(url: String): String {
        // Check if the URL starts with "https://"
        if (url.isNotBlank() && !url.startsWith(Constants.HTTPS)) {
            // Replace "http://" with "https://"
            return Constants.HTTPS + url.substringAfter("://")
        }
        return url
    }
}