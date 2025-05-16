package com.example.scstrade.repository

import android.content.Context
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.model.data.AccountOpening
import com.example.scstrade.model.data.AttorneyDetail
import com.example.scstrade.model.data.BasicData
import com.example.scstrade.model.data.ContactDetail
import kotlin.jvm.internal.Intrinsics.Kotlin
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class AofRepository (val context: Context){

    private val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val accountOpening = AccountOpening(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null)
    private val basicData = BasicData(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null)
    private val contactDetail = ContactDetail(null,null,null,null,null,null,null,null,null,null,null,null,null,null)
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

    fun saveReference(name: String) {
        sharedPreferences.edit().apply{
            putString(AppConstants.ACCOUNT_OPENING_REFERENCE,name)
            apply()
        }
    }

    fun getReference(): AccountOpening {
        accountOpening.reference = sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_REFERENCE,"")
        return accountOpening
    }


    fun saveBasicData(basicData: BasicData){
        sharedPreferences.edit().apply {
            if(basicData.uinType?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_UIN_TYPE,basicData.uinType)
            }

            if(basicData.uinNumber?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_UIN_NUMBER,basicData.uinNumber)
            }

            if(basicData.fullNicName?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_FULL_NIC_NAME,basicData.fullNicName)
            }

            if(basicData.salutation?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_SALUTATION,basicData.salutation)
            }

            if(basicData.dob?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_DOB,basicData.dob)
            }

            if(basicData.motherMaidenName?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_MOTHER_MAIDEN_NAME,basicData.motherMaidenName)
            }

            if(basicData.nationality?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_NATIONALITY,basicData.nationality)
            }

            if(basicData.maritalStatus?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_MARITAL_STATUS,basicData.maritalStatus)
            }

            if(basicData.relationShip?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_RELATIONSHIP,basicData.relationShip)
            }

            if(basicData.relationshipName?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_RELATIONSHIP_NAME,basicData.relationshipName)
            }

            if(basicData.nicType?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_NIC_TYPE,basicData.nicType)
            }

            if(basicData.nicValid?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_NIC_EXPIRY,basicData.nicValid)
            }

            if(basicData.pobCountry?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_POB_COUNTRY,basicData.pobCountry)
            }

            if(basicData.pobCity?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_POB_CITY,basicData.pobCity)
            }

            if(basicData.ivrService?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_IVR_SERVICE,basicData.ivrService)
            }
            apply()
        }
    }

    fun getbasicData(): BasicData {
        basicData.uinType = sharedPreferences.getString(AppConstants.BASIC_DATA_UIN_TYPE,"")
        basicData.uinNumber = sharedPreferences.getString(AppConstants.BASIC_DATA_UIN_NUMBER,"")
        basicData.salutation = sharedPreferences.getString(AppConstants.BASIC_DATA_SALUTATION,"")
        basicData.fullNicName = sharedPreferences.getString(AppConstants.BASIC_DATA_FULL_NIC_NAME,"")
        basicData.dob = sharedPreferences.getString(AppConstants.BASIC_DATA_DOB,"")
        basicData.motherMaidenName = sharedPreferences.getString(AppConstants.BASIC_DATA_MOTHER_MAIDEN_NAME,"")
        basicData.nationality = sharedPreferences.getString(AppConstants.BASIC_DATA_NATIONALITY,"")
        basicData.maritalStatus = sharedPreferences.getString(AppConstants.BASIC_DATA_MARITAL_STATUS,"")
        basicData.relationShip = sharedPreferences.getString(AppConstants.BASIC_DATA_RELATIONSHIP,"")
        basicData.relationshipName = sharedPreferences.getString(AppConstants.BASIC_DATA_RELATIONSHIP_NAME,"")
        basicData.nicType = sharedPreferences.getString(AppConstants.BASIC_DATA_NIC_TYPE,"")
        basicData.nicValid = sharedPreferences.getString(AppConstants.BASIC_DATA_NIC_EXPIRY,"")
        basicData.pobCountry = sharedPreferences.getString(AppConstants.BASIC_DATA_POB_COUNTRY,"")
        basicData.pobCity = sharedPreferences.getString(AppConstants.BASIC_DATA_POB_CITY,"")
        basicData.ivrService = sharedPreferences.getString(AppConstants.BASIC_DATA_IVR_SERVICE,"")
        return basicData
    }

    fun saveContactDetails(contactDetail: ContactDetail){

        sharedPreferences.edit().apply{
            val kClass = ContactDetail::class
            val properties = kClass.members.filterIsInstance<kotlin.reflect.KProperty1<Any, *>>()
            for (property in properties){
                val value = property.get(contactDetail)
                val name = property.name
                if(value!=null) {
                    if ((value as String).isNotEmpty()) {
                        putString(name, value)
                    }
                }
            }
            apply()
        }
    }

    fun getContactDetails(): ContactDetail? {
        val constructor = ContactDetail::class.primaryConstructor?:return null
        val args = constructor.parameters.associateWith { param ->
            val key = param.name ?: return@associateWith null
            when (param.type.classifier) {
                String::class -> sharedPreferences.getString(key, "")
                Int::class -> sharedPreferences.getInt(key, 0)
                Boolean::class -> sharedPreferences.getBoolean(key, false)
                Float::class -> sharedPreferences.getFloat(key, 0f)
                Long::class -> sharedPreferences.getLong(key, 0L)
                else -> null
            }
        }
        return constructor.callBy(args)
    }

    fun saveAttorneyDetails(attorneyDetail: AttorneyDetail) {
        sharedPreferences.edit().apply{
            val kClass = AttorneyDetail::class
            val properties = kClass.members.filterIsInstance<kotlin.reflect.KProperty1<Any, *>>()
            for (property in properties){
                val value = property.get(attorneyDetail)
                val name = property.name
                if(value!=null) {
                    if ((value as String).isNotEmpty()) {
                        putString(name, value)
                    }
                }
            }
            apply()
        }
    }

    fun getAttorneyDetails(): AttorneyDetail? {
        val constructor = AttorneyDetail::class.primaryConstructor?:return null
        val args = constructor.parameters.associateWith { param ->
            val key = param.name ?: return@associateWith null
            when (param.type.classifier) {
                String::class -> sharedPreferences.getString(key, "")
                Int::class -> sharedPreferences.getInt(key, 0)
                Boolean::class -> sharedPreferences.getBoolean(key, false)
                Float::class -> sharedPreferences.getFloat(key, 0f)
                Long::class -> sharedPreferences.getLong(key, 0L)
                else -> null
            }
        }
        return constructor.callBy(args)
    }


}