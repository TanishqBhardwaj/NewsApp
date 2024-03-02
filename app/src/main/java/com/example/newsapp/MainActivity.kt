package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.newsapp.network.NewsRequest
import com.example.newsapp.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private val newsViewModel by viewModels<NewsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        launch {
//            val apiRequest = NewsRequest()
//            val result = apiRequest.makeGetRequest("https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json")
//            // Handle the result here
//            println("---------------")
//            if (result != null) {
//                // Process the response
//                println(result.status)
//                println(result)
//            } else {
//                // Handle error
//                println("Error")
//            }
//        }
        newsViewModel.getNewsData()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel() // Cancels all coroutines started in the MainScope
    }
}