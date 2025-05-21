package com.example.scstrade.services

import android.util.Log
import android.widget.Toast
import com.example.scstrade.helper.Utils
import com.example.scstrade.views.MyApp
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Refreshed token: $token")

        // TODO: Send token to your backend server
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.notification?.let {
            // Show the toast message for foreground notification
            Toast.makeText(applicationContext, "Message Received: ${it.body}", Toast.LENGTH_LONG).show()
        }

        // Handle other data payload if necessary
        if (remoteMessage.data.isNotEmpty()) {
            // Handle data payload if needed
            Log.d("FCM", "Data payload: ${remoteMessage.data}")
        }
        // Handle foreground notification if needed
    }
}