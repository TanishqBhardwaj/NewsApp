package com.example.newsapp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.newsapp.R
import com.example.newsapp.utils.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * 1. Service to handle push notification
 * 2. Its not recommended to push google-services.json file (but pushing for code review)
 */
class PushNotificationService : FirebaseMessagingService() {

    // Handles the received message
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        var title = ""
        var description = ""
        for ((key, value) in message.data.entries) {
            when (key) {
                Constants.NOTIFICATION_TITLE -> title = value
                Constants.NOTIFICATION_DESCRIPTION -> description = value
            }
        }
        showNotification(title, description)
    }

    // For fetching refresh token
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    private fun showNotification(title: String?, message: String?) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                "Channel Name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        val builder: Notification.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
        } else {
            Notification.Builder(this)
        }
        builder
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_background)
        val notification: Notification = builder.build()
        notificationManager.notify(0, notification)
    }
}