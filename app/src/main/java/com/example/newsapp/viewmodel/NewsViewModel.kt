package com.example.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.newsapp.network.NewsRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
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
                    NewsUiState.Success(newsResponse.articles)
                }
            } else {
                _newsListState.update {
                    NewsUiState.Error(Constants.SOMETHING_WENT_WRONG)
                }
            }
        }
    }
}