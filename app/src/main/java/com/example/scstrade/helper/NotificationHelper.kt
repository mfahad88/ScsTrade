package com.example.scstrade.helper

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.scstrade.R
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.views.main.MainActivity
import com.example.scstrade.views.notification.NotificationDetailActivity

class NotificationHelper(private val context: Context) {

    private val CHANNEL_ID = "SCSTrade"
    private var isDashboard =false
    private var index=0
    fun createNotification(notificationMap: MutableMap<String, String>) {
        createNotificationChannel()

        Log.d(
            "Notification",
            notificationMap.toString()
            /*"Type ${notificationMap["type"].toString()}  Reference ID ${notificationMap["reference_id"].toString()} "*/
        )
        val activityToOpen = NotificationDetailActivity::class.java
        var intent = Intent(context, activityToOpen)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(AppConstants.ID_REF,notificationMap["reference_id"]?.toInt())
        intent.putExtra(AppConstants.ANNOUNCEMENT_TYPE_NAME,notificationMap["title"].toString())
        /*val loginOTPResponse = Utils.getLogin(context)

        //Log.d("Notification User Type","${notificationMap["userType"].toString()}")

        if(notificationMap["userType"].toString()!=SecurePreferences.getString(context,AppConstants.Keys.USER_TYPE)) {
            Intent(context, MainActivity::class.java)
            //Log.d("Notification User Type","${notificationMap["userType"].toString()} & matched ${loginOTPResponse?.userType}")
        }
        else {
            intent = getIntent(notificationMap, intent)
        }

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        if(!isDashboard) {
            intent.putExtra(
                AppConstants.SPKeys.PUSH_NOTIFICATION_ID,
                notificationMap["id"]
            ) // Replace with your actual parameter data
            isDashboard=false
        }
        intent.putExtra(
            AppConstants.Keys.IS_FROM_NOTIFICATION,
           true
        )*/



        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val title =notificationMap["title"]
        val company=notificationMap["company"]
//        val body = notificationMap["body"]
        val details = notificationMap["details"]?.replace("|","\n")?.trim()
        val boldTitle = SpannableString(title).apply {
            setSpan(StyleSpan(Typeface.BOLD), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        val collapsedContent = "$company"
        index += 1
// 👉 Combine full text for BigTextStyle
        val bigText = buildString {
            appendLine(company)
//            appendLine(body)
//            appendLine()
            appendLine(details)
        }

// ✅ Notification builder
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
//            .setColor(ContextCompat.getColor(context,R.color.md_theme_primary))
//            .setLargeIcon(BitmapFactory.decodeResource(context.resources,R.mipmap.ic_launcher_round))
            .setContentTitle(boldTitle)
            .setContentText(collapsedContent)
//            .setContentText(body) // shown below title in collapsed view
            .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }

            /*  var notificationToRoute = HashMap<String, Bundle>()
              if(SecurePreferences.contains(context,AppConstants.SPKeys.ROUTE_PUSH_NOTIFICATION)){
                  notificationToRoute = (SecurePreferences.getHashMap(context,AppConstants.SPKeys.ROUTE_PUSH_NOTIFICATION,Bundle::class.java) as HashMap<String, Bundle>?)?: HashMap()
              }
              notificationToRoute[notificationMap["id"].toString()] = Utils.intentToBundle(intent)
              SecurePreferences.saveHashMap(context,AppConstants.SPKeys.ROUTE_PUSH_NOTIFICATION,notificationToRoute)*/
            notify(notificationMap["reference_id"]?.toInt()?:0, notificationBuilder.build())
        }
    }



    private fun createNotificationChannel() {
        val name = context.getString(R.string.app_name)
        val descriptionText = "ScsTrade Pro"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            getSystemService(context, NotificationManager::class.java) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}