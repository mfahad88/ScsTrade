package com.example.scstrade.model.response.aof.register


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("email")
    val email: String
)