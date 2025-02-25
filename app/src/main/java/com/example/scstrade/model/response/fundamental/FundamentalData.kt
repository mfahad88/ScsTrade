package com.example.scstrade.model.response.fundamental


import com.google.gson.annotations.SerializedName

data class FundamentalData(
    @SerializedName("Fundamentals")
    val fundamentals: String
)