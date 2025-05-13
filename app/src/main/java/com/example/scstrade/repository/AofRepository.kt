package com.example.scstrade.repository

import android.content.Context
import com.example.scstrade.helper.AppConstants

class AofRepository (val context: Context){

    private val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    public fun saveSelfInfo(fname:String,email:String, residential:String,nicType: String,nicNumber:String){
        sharedPreferences.edit().apply {
            putString(AppConstants.ACCOUNT_OPENING_FULLNAME,fname)
            putString(AppConstants.ACCOUNT_OPENING_EMAIL,email)
            putString(AppConstants.ACCOUNT_OPENING_RESIDENTIAL,residential)
            putString(AppConstants.ACCOUNT_OPENING_NIC_TYPE,nicType)
            putString(AppConstants.ACCOUNT_OPENING_NIC_NUMBER,nicNumber)
            apply()
        }
    }

    public fun getSelfInfo(): List<String?> {
        return listOf( sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_FULLNAME,""),
        sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_EMAIL,""),
        sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_RESIDENTIAL,""),
        sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_NIC_TYPE,""),
        sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_NIC_NUMBER,"")
        )

    }
}