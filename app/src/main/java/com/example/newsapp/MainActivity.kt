package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newsapp.network.NewsRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launch {
            val apiRequest = NewsRequest()
            val result = apiRequest.makeGetRequest("https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json")
            // Handle the result here
            println("---------------")
            if (result != null) {
                // Process the response
                println(result.status)
                println(result)
            } else {
                // Handle error
                println("Error")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel() // Cancels all coroutines started in the MainScope
    }
}