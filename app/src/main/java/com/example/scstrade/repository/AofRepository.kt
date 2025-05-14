package com.example.scstrade.repository

import android.content.Context
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.model.data.AccountOpening

class AofRepository (val context: Context){

    private val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val accountOpening = AccountOpening(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null)
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

    fun saveContactIban(mobileNumber: String, registerUnder: String, iban:String) {
        sharedPreferences.edit().apply {
            putString(AppConstants.ACCOUNT_OPENING_MOBILE_NUMBER,mobileNumber)
            putString(AppConstants.ACCOUNT_OPENING_REGISTERED_UNDER,registerUnder)
            putString(AppConstants.ACCOUNT_OPENING_BANK_IBAN,iban)
            apply()
        }
    }

    fun getContactIban(): AccountOpening {
        accountOpening.mobileNumber = sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_MOBILE_NUMBER,"")
        accountOpening.registerUnder = sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_REGISTERED_UNDER,"")
        accountOpening.ibanNumber = sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_BANK_IBAN,"")
        return accountOpening
    }

    fun saveDocuments(ibanFileName:String,iban:String,nicFrontFileName:String,nicFront:String,nicBackFileName:String,nicBack:String){
        sharedPreferences.edit().apply{
            putString(AppConstants.DOCUMENT_IBAN_NAME,ibanFileName)
            putString(AppConstants.DOCUMENT_IBAN,iban)
            putString(AppConstants.DOCUMENT_NIC_FRONT_NAME,nicFrontFileName)
            putString(AppConstants.DOCUMENT_NIC_FRONT,nicFront)
            putString(AppConstants.DOCUMENT_NIC_BACK_NAME,nicBackFileName)
            putString(AppConstants.DOCUMENT_NIC_BACK,nicBack)
            apply()
        }
    }

    fun getDocuments(): AccountOpening {
        accountOpening.proofIban=sharedPreferences.getString(AppConstants.DOCUMENT_IBAN_NAME,"")
        accountOpening.proofIbanImage=sharedPreferences.getString(AppConstants.DOCUMENT_IBAN,"")

        accountOpening.nicFront=sharedPreferences.getString(AppConstants.DOCUMENT_NIC_FRONT_NAME,"")
        accountOpening.nicFrontImage=sharedPreferences.getString(AppConstants.DOCUMENT_NIC_FRONT,"")

        accountOpening.nicBack=sharedPreferences.getString(AppConstants.DOCUMENT_NIC_BACK_NAME,"")
        accountOpening.nicBackImage=sharedPreferences.getString(AppConstants.DOCUMENT_NIC_BACK,"")

        return accountOpening
    }
}