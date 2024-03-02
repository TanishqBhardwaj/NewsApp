package com.example.newsapp.model

sealed class NewsUiState {
    data object Loading : NewsUiState()
    data class Success(val data: List<NewsItem>) : NewsUiState()
    data class Error(val message: String) : NewsUiState()
}