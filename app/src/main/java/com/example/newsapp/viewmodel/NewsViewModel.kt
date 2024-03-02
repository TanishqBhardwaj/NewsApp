package com.example.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.newsapp.network.NewsRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class NewsViewModel @Inject constructor(private val newsRequest: NewsRequest): ViewModel() {

    fun getNewsData() {
        viewModelScope.launch(Dispatchers.IO) {
            val newsResponse = newsRequest.makeGetRequest("https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json")
            println("---------------")
            if (newsResponse != null) {
                // Process the response
                println(newsResponse.status)
                println(newsResponse)
            } else {
                // Handle error
                println("Error")
            }
        }
    }
}