package com.example.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.newsapp.network.NewsRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.NewsItem
import com.example.newsapp.model.NewsUiState
import com.example.newsapp.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@HiltViewModel
class NewsViewModel @Inject constructor(private val newsRequest: NewsRequest): ViewModel() {

    private val _newsListState: MutableStateFlow<NewsUiState> = MutableStateFlow(NewsUiState.Loading)
    val newsListState get() = _newsListState.asStateFlow()

    fun getNewsData(latestInd: Boolean = true) {
        _newsListState.update {
            NewsUiState.Loading
        }
        // Data is already present, no need to re-fetch data
        if (_newsListState.value is NewsUiState.Success) {
            _newsListState.update {
                NewsUiState.Success(getComputedArticles((_newsListState.value as NewsUiState.Success).data, latestInd))
            }
        } else {
            /**
             * 1. This scope gets automatically destroyed, when the view model destroys
             * 2. Launching coroutine for IO operation
             */
            viewModelScope.launch(Dispatchers.IO) {
                val newsResponse = newsRequest.makeGetRequest(Constants.BASE_URL)
                if (newsResponse != null) {
                    _newsListState.update {
                        NewsUiState.Success(getComputedArticles(newsResponse.articles, latestInd))
                    }
                } else {
                    _newsListState.update {
                        NewsUiState.Error(Constants.SOMETHING_WENT_WRONG)
                    }
                }
            }
        }
    }

    /**
     * To get computed article list (with corrected url)
     * @param newsItemList the list to be computed
     * @param latestInd the indicator on the basis of which list needs to be sorted
     * @return the computed list
     */
    private fun getComputedArticles(newsItemList: List<NewsItem>, latestInd: Boolean): List<NewsItem> {
        val newsList = mutableListOf<NewsItem>()
        for (newsItem in newsItemList) {
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
        if (latestInd) {
            return newsList.sortedByDescending {
                it.publishedAt?.let { it1 ->
                    SimpleDateFormat(
                        Constants.DATE_FORMAT,
                        Locale.getDefault()
                    ).parse(it1)
                }
            }
        }
        return newsList.sortedBy {
               it.publishedAt?.let { it1 ->
                   SimpleDateFormat(
                       Constants.DATE_FORMAT,
                       Locale.getDefault()
                   ).parse(it1)
               }
        }
    }

    // converting this to improve security
    private fun ensureHttpsUrl(url: String): String {
        // Check if the URL starts with "https://"
        if (url.isNotBlank() && !url.startsWith(Constants.HTTPS)) {
            // Replace "http://" with "https://"
            return Constants.HTTPS + url.substringAfter("://")
        }
        return url
    }
}