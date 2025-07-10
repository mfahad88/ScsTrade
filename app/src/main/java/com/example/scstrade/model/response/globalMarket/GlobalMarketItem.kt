package com.example.scstrade.model.response.globalMarket


import com.google.gson.annotations.SerializedName

data class GlobalMarketItem(
    @SerializedName("WorldMarketChange")
    val worldMarketChange: Double,
    @SerializedName("WorldMarketChangeP")
    val worldMarketChangeP: Double,
    @SerializedName("WorldMarketHigh")
    val worldMarketHigh: Double,
    @SerializedName("WorldMarketID")
    val worldMarketID: Int,
    @SerializedName("WorldMarketLow")
    val worldMarketLow: Double,
    @SerializedName("WorldMarketName")
    val worldMarketName: String,
    @SerializedName("WorldMarketPrice")
    val worldMarketPrice: Double,
    @SerializedName("WorldMarketType")
    val worldMarketType: String
)