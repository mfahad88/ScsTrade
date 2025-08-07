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
import android.text.SpannableStringBuilder
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

/*
class NotificationHelper(private val context: Context) {

    private val CHANNEL_ID = "SCSTrade"
    private var isDashboard =false
    private var index=0
    fun createNotification(notificationMap: MutableMap<String, String>) {
        createNotificationChannel()

        Log.d(
            "Notification",
            notificationMap.toString()
            */
/*"Type ${notificationMap["type"].toString()}  Reference ID ${notificationMap["reference_id"].toString()} "*//*

        )
        val requestCode = notificationMap["reference_id"]?.toIntOrNull() ?: System.currentTimeMillis().toInt()
        val activityToOpen = MainActivity::class.java
        var intent = Intent(context, activityToOpen)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(AppConstants.IS_NOTIFY,true)
        intent.putExtra(AppConstants.ID_REF,notificationMap["reference_id"]?.toInt())
        intent.putExtra(AppConstants.ANNOUNCEMENT_TYPE_NAME,notificationMap["title"].toString())




        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val title =notificationMap["title"]
        val company=notificationMap["company"]
//        val body = notificationMap["body"]

        val details = notificationMap["details"]?.replace("| ","\n")?.trim()

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
            appendLine(formatDetailsWithBoldKeys(details))
        }

// ✅ Notification builder
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
//            .setColor(ContextCompat.getColor(context,R.color.md_theme_primary))
//            .setLargeIcon(BitmapFactory.decodeResource(context.resources,R.mipmap.ic_launcher_round))
            .setContentTitle(boldTitle)
            .setContentText(collapsedContent)
//            .setContentText(body) // shown below title in collapsed view
            .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)


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


            notify(notificationMap["reference_id"]?.toInt()?:System.currentTimeMillis().toInt(), notificationBuilder.build())
        }
    }

    fun formatDetailsWithBoldKeys(details: String?): SpannableStringBuilder {
        val builder = SpannableStringBuilder()
        if(details!=null){
            val parts = details.split("|")
            for (part in parts) {
                val line = part.trim()
                if (line.contains(":")) {
                    val key = line.substringBefore(":").trim()
                    val value = line.substringAfter(":").trim()

                    // ✅ Skip if value is empty, null, or blank
                    if (value.isBlank() || value.equals("null", ignoreCase = true)) continue

                    val start = builder.length
                    builder.append("$key: ")
                    builder.setSpan(StyleSpan(Typeface.BOLD), start, start + key.length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    builder.append(value)
                    builder.append("\n")
                }
            }
        }else{
            builder.append("")
        }
        return builder
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

}*/

class NotificationHelper(private val context: Context) {

    private val CHANNEL_ID = "SCSTrade"

    fun createNotification(notificationMap: MutableMap<String, String?>) {
        createNotificationChannel()

        Log.d("Notification", notificationMap.toString())

        val refId = notificationMap["reference_id"]?.toIntOrNull() ?: System.currentTimeMillis().toInt()
        val title = notificationMap["title"] ?: "SCSTrade Notification"
        val company = notificationMap["company"] ?: ""
        val details = notificationMap["details"]?.trim()/*replace("| ", "\n")?.trim()*/
        var intent:Intent?=null
        // Launch MainActivity and it will redirect to NotificationDetailActivity
        if(title.equals("Market Updates",true)){
            intent = Intent(context, MainActivity::class.java).apply {
                flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(AppConstants.IS_MARKET, true)
            }

        }else if(title.contains("Analyst Opinion",true)){
            val type=title.substringAfter("-").trim()
            intent = Intent(context, MainActivity::class.java).apply {
                flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(AppConstants.IS_ANALYST, true)
                putExtra(AppConstants.OPINION_TYPE,type)
            }
        }
        else {
            intent = Intent(context, MainActivity::class.java).apply {
                flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(AppConstants.IS_NOTIFY, true)
                putExtra(AppConstants.ID_REF, notificationMap["reference_id"]?.toIntOrNull() ?: -1)
                putExtra(AppConstants.ANNOUNCEMENT_TYPE_NAME, title)
            }

        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            refId,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val boldTitle = SpannableString(title).apply {
            setSpan(StyleSpan(Typeface.BOLD), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        val bigText = buildString {
            appendLine(company)
            appendLine(formatDetailsWithBoldKeys(details))
        }

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(boldTitle)
            .setContentText(company)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        try {
            with(NotificationManagerCompat.from(context)) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.w("NotificationHelper", "POST_NOTIFICATIONS permission not granted.")
                    return
                }
                notify(refId, notificationBuilder.build())
            }
        } catch (e: Exception) {
            Log.e("NotificationHelper", "Error sending notification: ${e.message}")
        }
    }

    fun formatDetailsWithBoldKeys(details: String?): SpannableStringBuilder {
        val builder = SpannableStringBuilder()

        if (!details.isNullOrBlank()) {
            if(details.contains("|")){
                val parts = details.split("|")
                // Filter out invalid lines first
                val validLines = parts.mapNotNull { part ->
                    val line = part.trim()
                    if (line.contains(":")) {
                        val key = line.substringBefore(":").trim()
                        val value = line.substringAfter(":").trim()

                        if (key.isBlank() || value.isBlank() || value.equals("null", ignoreCase = true)) return@mapNotNull null
                        Pair(key, value)
                    } else null
                }

                // Loop through validLines and only append \n for non-last items
                for ((index, pair) in validLines.withIndex()) {
                    val (key, value) = pair
                    Log.e("Notification:", "$key\n$value")
                    val start = builder.length
                    builder.append("$key: ")
                    builder.setSpan(
                        StyleSpan(Typeface.BOLD),
                        start,
                        start + key.length + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    builder.append(value)
                    if (index != validLines.lastIndex) {
                        builder.append("\n")
                    }
                }
            }else{
                builder.append(details)
            }
        }

        return builder
        /*val builder = SpannableStringBuilder()
        if (details != null) {
            val parts = details.split("|")
            for (part in parts) {
                val line = part.trim()
                if (line.contains(":")) {
                    val key = line.substringBefore(":").trim()
                    val value = line.substringAfter(":").trim()

                    if (value.isBlank() || value.equals("null", ignoreCase = true)) continue
                    Log.e("Notification: ","$key\n$value")
                    val start = builder.length
                    builder.append("$key: ")
                    builder.setSpan(StyleSpan(Typeface.BOLD), start, start + key.length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    builder.append(value)
                    builder.append("\n")
                }
            }
        }
        return builder*/
    }

    private fun createNotificationChannel() {
        val name = context.getString(R.string.app_name)
        val descriptionText = "SCSTrade Pro Notifications"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager = getSystemService(context, NotificationManager::class.java) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
