package com.example.scstrade.model.response.aof.login


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("access_token")
    val accessToken: String
)