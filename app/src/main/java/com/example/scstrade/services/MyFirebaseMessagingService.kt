package com.example.scstrade.services

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.scstrade.R
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.NotificationHelper
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.notification.NotificationDetailActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    val notificationHelper = NotificationHelper(this)
    lateinit var sharedViewModel:SharedViewModel
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Refreshed token: $token")

        // TODO: Send token to your backend server
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        sharedViewModel = (application as MyApp).viewModel
        Log.d("FCM", "Data payload: ${remoteMessage.data}")
        notificationHelper.createNotification(remoteMessage.data)
        sharedViewModel.isHome=true

    }
}