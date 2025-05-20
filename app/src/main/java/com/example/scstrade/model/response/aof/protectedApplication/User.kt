package com.example.scstrade.model.response.aof.protectedApplication


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("email")
    val email: String,
    @SerializedName("expiresAt")
    val expiresAt: String,
    @SerializedName("identificationType")
    val identificationType: String,
    @SerializedName("issuedAt")
    val issuedAt: String,
    @SerializedName("lifecycleStatusId")
    val lifecycleStatusId: Int,
    @SerializedName("mobileNo")
    val mobileNo: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("sub")
    val sub: String,
    @SerializedName("uin")
    val uin: String
)