package com.example.scstrade.model.response.login


import com.google.gson.annotations.SerializedName

data class LoginDataItem(
    @SerializedName("RegistrationDate")
    val registrationDate: String,
    @SerializedName("RegistrationEmail")
    val registrationEmail: String,
    @SerializedName("RegistrationID")
    val registrationID: Int,
    @SerializedName("RegistrationLastName")
    val registrationLastName: String,
    @SerializedName("RegistrationName")
    val registrationName: String,
    @SerializedName("RegistrationPassword")
    val registrationPassword: String,
    @SerializedName("RegistrationPhone")
    val registrationPhone: String,
    @SerializedName("RegistrationStatus")
    val registrationStatus: Boolean
)