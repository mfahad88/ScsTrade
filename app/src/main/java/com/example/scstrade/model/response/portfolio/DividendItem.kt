package com.example.scstrade.model.response.portfolio


import com.google.gson.annotations.SerializedName

data class DividendItem(
    @SerializedName("DividendDate")
    val dividendDate: String,
    @SerializedName("DividendID")
    val dividendID: Int,
    @SerializedName("DividendPerShare")
    val dividendPerShare: Int,
    @SerializedName("DividendQuantity")
    val dividendQuantity: Int,
    @SerializedName("DividendSymbol")
    val dividendSymbol: String,
    @SerializedName("PortfolioMainID")
    val portfolioMainID: Int
)