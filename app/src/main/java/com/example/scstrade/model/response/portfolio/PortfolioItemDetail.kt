package com.example.scstrade.model.response.portfolio


import com.google.gson.annotations.SerializedName

data class PortfolioItemDetail(
    @SerializedName("Date")
    val date: String,
    @SerializedName("Quantity")
    val quantity: String,
    @SerializedName("Rate")
    val rate: String,
    val currentPL:Double,
    val currentPercentPL:Double
)