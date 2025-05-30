package com.example.scstrade.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.scstrade.R
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.views.main.MainActivity
import com.example.scstrade.views.notification.NotificationDetailActivity

class NotificationHelper(private val context: Context) {

    private val CHANNEL_ID = "tlh"
    private var isDashboard =false

    fun createNotification(notificationMap: MutableMap<String, String>) {
        createNotificationChannel()

        Log.d(
            "Notification",
            "Type ${notificationMap["type"].toString()}  Reference ID ${notificationMap["reference_id"].toString()} "
        )
        val activityToOpen = NotificationDetailActivity::class.java
        var intent = Intent(context, activityToOpen)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(AppConstants.ID_REF,notificationMap["reference_id"]?.toInt())
        intent.putExtra(AppConstants.ANNOUNCEMENT_TYPE_NAME,notificationMap["type"].toString())
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

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(notificationMap["title"])
            .setContentText(notificationMap["message"])
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
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

            /*  var notificationToRoute = HashMap<String, Bundle>()
              if(SecurePreferences.contains(context,AppConstants.SPKeys.ROUTE_PUSH_NOTIFICATION)){
                  notificationToRoute = (SecurePreferences.getHashMap(context,AppConstants.SPKeys.ROUTE_PUSH_NOTIFICATION,Bundle::class.java) as HashMap<String, Bundle>?)?: HashMap()
              }
              notificationToRoute[notificationMap["id"].toString()] = Utils.intentToBundle(intent)
              SecurePreferences.saveHashMap(context,AppConstants.SPKeys.ROUTE_PUSH_NOTIFICATION,notificationToRoute)*/
            notify(0, notificationBuilder.build())
        }
    }

    /*private fun getIntent(
        notificationMap: MutableMap<String, String>,
        intent: Intent
    ): Intent {
        var intent1 = intent
       *//* if (notificationMap["type"].toString() == AppConstants.PushNotificationTypes.BORROWER_LOAN_APPROVED_SUCCESS) {
            intent1 = Intent(context, LoanDetailActivity::class.java)
            intent1.putExtra(AppConstants.Keys.LOAN_ID, notificationMap["loanId"])
        }*//*
        when (notificationMap["type"].toString()) {
            AppConstants.PushNotificationTypes.BORROWER_INSTALLMENT_UPCOMING, AppConstants.PushNotificationTypes.BORROWER_INSTALLMENT_OVERDUE, AppConstants.PushNotificationTypes.BORROWER_INSTALLMENT_DEFAULTED-> {
                intent1 = Intent(context, LoanDetailActivity::class.java)
                intent1.putExtra(AppConstants.Keys.LOAN_ID, notificationMap["loanId"])
            }
            AppConstants.PushNotificationTypes.BORROWER_LOAN_APPROVED_SUCCESS, AppConstants.PushNotificationTypes.BORROWER_LOAN_REJECTED_BY_BORROWER, AppConstants.PushNotificationTypes.BORROWER_HIGH_RISK_SCORE_LOAN_REJECT, AppConstants.PushNotificationTypes.BORROWER_LOAN_APPLICATION_EXPIRED, AppConstants.PushNotificationTypes.BORROWER_LOAN_APPLICATION_UNDER_REVIEW  -> {
                intent1 = Intent(context, ApplicationDetailActivity::class.java)
                intent1.putExtra(AppConstants.Keys.APPLICATION_LOAN_ID, notificationMap["applicationLoanId"])
            }
            AppConstants.PushNotificationTypes.LENDER_LOAN_REPAYMENT_RECEIVED, AppConstants.PushNotificationTypes.LENDER_FOUR_INVESTORS_INVESTED -> {
                intent1 = Intent(context, InvestmentDetailActivity::class.java)
                intent1.putExtra(AppConstants.Keys.LOAN_ID, notificationMap["loanId"])
            }
            AppConstants.PushNotificationTypes.ADMIN_BAN_USER, AppConstants.PushNotificationTypes.ADMIN_UNBAN_USER, AppConstants.PushNotificationTypes.ADMIN_PEP_APPROVE
                , AppConstants.PushNotificationTypes.ADMIN_PEP_REJECT, AppConstants.PushNotificationTypes.ADMIN_SANCTIONED_APPROVE, AppConstants.PushNotificationTypes.ADMIN_SANCTIONED_REJECT, AppConstants.PushNotificationTypes.ADMIN_CANCELLED_LOAN-> {
                intent1 = Intent(context, MainActivity::class.java)
                isDashboard = true
            }
            AppConstants.PushNotificationTypes.LENDER_NEW_OPPORTUNITY , AppConstants.PushNotificationTypes.LENDER_LOAN_EXPIRY-> {
                intent1 = Intent(context, InvestmentOpportunityDetailActivity::class.java)
                intent1.putExtra(AppConstants.Keys.LOAN_ID, notificationMap["loanId"])
                intent1.putExtra(AppConstants.Keys.APPLICATION_LOAN_ID, notificationMap["applicationLoanId"])
                intent1.putExtra(AppConstants.Keys.OPPORTUNITY_TYPE, notificationMap["opportunityType"])
            }
            AppConstants.PushNotificationTypes.BORROWER_WALLET_AMOUNT_RECEIVED -> {
                intent1 = Intent(context, TransactionDetailActivity::class.java)
                intent1.putExtra(AppConstants.Keys.TRANSACTION_ID, notificationMap["transactionId"])
            }

            else -> Intent(context, MainActivity::class.java)
        }
        return intent1
    }*/

    private fun createNotificationChannel() {
        val name = context.getString(R.string.app_name)
        val descriptionText = "ScsTrade Pro"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            getSystemService(context, NotificationManager::class.java) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}