package com.example.scstrade.model.request.aof.verifyOtp


import com.google.gson.annotations.SerializedName

data class VerifyOtpDto(
    @SerializedName("otp")
    val otp: String,
  /*  @SerializedName("request")
    val request: String,
    @SerializedName("uin")
    val uin: String*/
)