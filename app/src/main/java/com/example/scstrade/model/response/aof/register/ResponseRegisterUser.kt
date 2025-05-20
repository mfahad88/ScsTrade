package com.example.scstrade.model.response.aof.register


import com.google.gson.annotations.SerializedName

data class ResponseRegisterUser(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("isSuccess")
    val isSuccess: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("statusCode")
    val statusCode: Int
)