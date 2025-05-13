package com.example.scstrade.repository

import android.content.Context
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.model.data.AccountOpening

class AofRepository (val context: Context){

    private val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val accountOpening = AccountOpening(null,null,null,null,null,null,null,null,null,null,null,null)
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

    public fun getSelfInfo(): AccountOpening {
        accountOpening.fullName = sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_FULLNAME,"")
        accountOpening.emailAddress = sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_EMAIL,"")
        accountOpening.residentialStatus = sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_RESIDENTIAL,"")
        accountOpening.nicType = sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_NIC_TYPE,"")
        accountOpening.nicNumber = sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_NIC_NUMBER,"")
        return accountOpening

    }
}