package com.example.scstrade.model.response.portfolio


import com.google.gson.annotations.SerializedName

data class CloseTrade(
    @SerializedName("PortfolioMainID")
    val portfolioMainID: String,
    @SerializedName("PurAmount")
    val purAmount: String,
    @SerializedName("PurDate")
    val purDate: String,
    @SerializedName("PurPrice")
    val purPrice: String,
    @SerializedName("PurQuantity")
    val purQuantity: String,
    @SerializedName("SalAmount")
    val salAmount: String,
    @SerializedName("SalDate")
    val salDate: String,
    @SerializedName("SalPrice")
    val salPrice: String,
    @SerializedName("SalQuantity")
    val salQuantity: String,
    @SerializedName("Symbol")
    val symbol: String
)