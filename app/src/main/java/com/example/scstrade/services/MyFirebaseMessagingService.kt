package com.example.scstrade.services

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.scstrade.R
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.views.notification.NotificationDetailActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    val notificationHelper = NotificationHelper(this)
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Refreshed token: $token")

        // TODO: Send token to your backend server
    }
    private fun showNotification(title: String?, message: String?) {
        if(message?.contains("$")?:false) {
            val intent = Intent(this, NotificationDetailActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            intent.putExtra(AppConstants.ID_REF,message?.substring(message?.indexOf("$")?:0+1,message?.indexOf("|")?:0-1))
            intent.putExtra(AppConstants.ANNOUNCEMENT_TYPE_NAME,message?.substring(message?.indexOf("|")?:0+1,message?.length?:0))
            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val builder = NotificationCompat.Builder(this, "firebase_channel")
                .setSmallIcon(R.drawable.logo) // Add this icon in your drawable folder
                .setContentTitle(title)
                .setContentText(message?.subSequence(0,message.indexOf("$")-1))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            val manager = NotificationManagerCompat.from(this)
            manager.notify(101, builder.build())

        }else{
            val builder = NotificationCompat.Builder(this, "firebase_channel")
                .setSmallIcon(R.drawable.logo) // Add this icon in your drawable folder
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

            val manager = NotificationManagerCompat.from(this)
            manager.notify(101, builder.build())
        }

     /*   intent.putExtra(AppConstants.ID_REF,it.MainAnnIDRef)
        intent.putExtra(AppConstants.ANNOUNCEMENT_TYPE_NAME,it.AnnouncementTypeName)
        startActivity(intent)*/


    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM", "Data payload: ${remoteMessage.data}")
        notificationHelper.createNotification(remoteMessage.data)
        /*Log.e("fcm", remoteMessage.data.toString())

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
        }*/
        // Handle foreground notification if needed
    }
}