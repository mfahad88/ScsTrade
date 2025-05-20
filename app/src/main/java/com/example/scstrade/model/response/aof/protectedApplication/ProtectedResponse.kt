package com.example.scstrade.model.response.aof.protectedApplication


import com.google.gson.annotations.SerializedName

data class ProtectedResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("user")
    val user: User
)