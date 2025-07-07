package com.example.scstrade.model.request.aof


import com.google.gson.annotations.SerializedName

data class RegisterUser(
    @SerializedName("applicationId")
    val applicationId: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("ibanNo")
    val ibanNo: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("identificationType")
    val identificationType: String,
    @SerializedName("isApp")
    val isApp: Boolean,
    @SerializedName("issueDate")
    val issueDate: String,
    @SerializedName("lifecycleStatus")
    val lifecycleStatus: Int,
    @SerializedName("mobileNo")
    val mobileNo: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("nicBack")
    val nicBack: String,
    @SerializedName("nicFront")
    val nicFront: String,
    @SerializedName("proofofIBAN")
    val proofofIBAN: String,
    @SerializedName("proofofRelationships")
    val proofofRelationships: String,
    @SerializedName("reference")
    val reference: String,
    @SerializedName("relationship")
    val relationship: String,
    @SerializedName("relativeName")
    val relativeName: String,
    @SerializedName("relativeUIN")
    val relativeUIN: String,
    @SerializedName("residentialStatus")
    val residentialStatus: String,
    @SerializedName("uin")
    val uin: String
)