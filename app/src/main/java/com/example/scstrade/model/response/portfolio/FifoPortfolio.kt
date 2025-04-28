package com.example.scstrade.model.response.portfolio


import com.google.gson.annotations.SerializedName

data class FifoPortfolio(
    @SerializedName("PortfolioMainID")
    val portfolioMainID: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("quantity")
    val quantity: String,
    @SerializedName("symbol")
    val symbol: String
)