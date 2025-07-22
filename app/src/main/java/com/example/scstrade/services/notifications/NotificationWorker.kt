package com.example.scstrade.services.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.scstrade.R
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.services.ApiService
import com.example.scstrade.services.RetrofitInstance
import com.example.scstrade.views.main.MainActivity

class NotificationWorker(context: Context, workerParams: WorkerParameters): Worker(context, workerParams) {
    val retrofit = RetrofitInstance.create(ApiService::class.java)
    override fun doWork(): Result {

        val response=retrofit.getIndicess().execute()
        if(response.isSuccessful) {
            val stringBuilder=StringBuilder()
            response.body()?.forEach {
                stringBuilder.append(createKseStatusMessage(it))
            }
            Log.d("NotificationWorker", "Worker triggered! Generating PSX market notification.")

            showNotification(stringBuilder.toString())
        }
        return Result.success()
    }

    fun createKseStatusMessage(data: KSEIndices): String {
        val currentIndex = data.cURRENTINDEX.toDoubleOrNull() ?: 0.0
        val netChange = data.nETCHANGE.toDoubleOrNull() ?: 0.0
        val preClose = data.preClose
        val changePercent = if (preClose != 0.0) (netChange / preClose) * 100 else 0.0

        val arrow = if (netChange >= 0) "📈" else "📉"
        val statusLine = "Market is currently: ${data.marketStatus}"
        val indexLine = "$arrow ${data.iNDEXCODE}: ${"%.2f".format(currentIndex)} " +
                "(${if (netChange >= 0) "+" else ""}${"%.2f".format(netChange)} | ${"%.2f".format(changePercent)}%)"
        val highLowLine = "High: ${data.hIGHINDEX} | Low: ${data.lOWINDEX}"
        val volumeValueLine = "Vol: ${data.vOLUMETRADED} | Val: ${data.vALUETRADED}"

        return "$statusLine\n$indexLine\n$highLowLine\n$volumeValueLine"
    }
    private fun showNotification(message:String) {
        val channelId = "market_channel"
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Market Alerts", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("PSX Market Status")
            .setContentText("Tap to expand for full market info")
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(2001, notification)
    }
}