package com.example.scstrade.model.response.contact


import com.google.gson.annotations.SerializedName

data class ContactData(
    @SerializedName("ContactUsAddress")
    val contactUsAddress: String?,
    @SerializedName("ContactUsBranchManager")
    val contactUsBranchManager: String?,
    @SerializedName("ContactUsEmail")
    val contactUsEmail: String?,
    @SerializedName("ContactUsID")
    val contactUsID: Int,
    @SerializedName("ContactUsMap")
    val contactUsMap: String?,
    @SerializedName("ContactUsName")
    val contactUsName: String,
    @SerializedName("ContactUsPhone")
    val contactUsPhone: String,
    @SerializedName("ContactUsWhatsApp")
    val contactUsWhatsApp: String?
)