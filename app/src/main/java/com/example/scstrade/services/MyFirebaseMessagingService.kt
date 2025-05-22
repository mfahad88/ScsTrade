package com.example.scstrade.services

import android.R
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Refreshed token: $token")

        // TODO: Send token to your backend server
    }
    private fun showNotification(title: String?, message: String?) {
        val builder = NotificationCompat.Builder(this, "firebase_channel")
            .setSmallIcon(R.drawable.ic_media_play) // Add this icon in your drawable folder
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val manager = NotificationManagerCompat.from(this)
        manager.notify(101, builder.build())
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.notification != null) {
            showNotification(
                remoteMessage.notification!!.title,
                remoteMessage.notification!!.body
            )
        }

        // Handle other data payload if necessary
        if (remoteMessage.data.isNotEmpty()) {
            // Handle data payload if needed
            Log.d("FCM", "Data payload: ${remoteMessage.data}")
        }
        // Handle foreground notification if needed
    }
}