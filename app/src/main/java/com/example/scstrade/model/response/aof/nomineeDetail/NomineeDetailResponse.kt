package com.example.scstrade.model.response.aof.nomineeDetail


import com.google.gson.annotations.SerializedName

data class NomineeDetailResponse(
    @SerializedName("addressNmn")
    val addressNmn: String,
    @SerializedName("applicationId")
    val applicationId: Int,
    @SerializedName("cnicExpiryDateNmn")
    val cnicExpiryDateNmn: String,
    @SerializedName("cnicLifeTimeNmn")
    val cnicLifeTimeNmn: String,
    @SerializedName("cnicNmn")
    val cnicNmn: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("identificationNmn")
    val identificationNmn: String,
    @SerializedName("mobileNoNmn")
    val mobileNoNmn: String,
    @SerializedName("nameNmn")
    val nameNmn: String,
    @SerializedName("nicBackNmn")
    val nicBackNmn: String,
    @SerializedName("nicFrontNmn")
    val nicFrontNmn: String,
    @SerializedName("nomineeType")
    val nomineeType: String,
    @SerializedName("relationShipNmn")
    val relationShipNmn: String
)