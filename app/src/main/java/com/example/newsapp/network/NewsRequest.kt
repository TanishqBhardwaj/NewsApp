package com.example.newsapp.network

import com.example.newsapp.model.NewsResponse
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream

class NewsRequest {

    suspend fun makeGetRequest(urlString: String): NewsResponse? {
        return withContext(Dispatchers.IO) {
            var newsResponse: NewsResponse? = null
            val url = URL(urlString)
            val urlConnection = url.openConnection() as HttpURLConnection
            try {
                val inputStream: InputStream = urlConnection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                newsResponse = Gson().fromJson(reader, NewsResponse::class.java)
                // closing the streams
                reader.close()
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                // disconnecting the connection
                urlConnection.disconnect()
            }
            newsResponse
        }
    }
}
