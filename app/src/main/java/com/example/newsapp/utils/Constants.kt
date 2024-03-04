package com.example.newsapp.utils

object Constants {
    const val BASE_URL = "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json"
    const val HTTPS = "https://"
    const val ARTICLE_URL = "ARTICLE_URL"
    const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    const val NOTIFICATION_TITLE = "title"
    const val NOTIFICATION_DESCRIPTION = "description"
    const val NOTIFICATION_CHANNEL_ID = "news_channel"

    // Messages
    const val SOMETHING_WENT_WRONG = "Something went wrong!"
    const val NOTIFICATION_PERMISSION_ERROR = "Permission for notification not given!"
    const val FETCHING_FCM_TOKEN_FAILED = "Fetching FCM registration token failed"
}