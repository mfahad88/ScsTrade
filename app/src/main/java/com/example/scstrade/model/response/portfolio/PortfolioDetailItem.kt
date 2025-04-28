package com.example.scstrade.model.response.portfolio


import com.google.gson.annotations.SerializedName

data class PortfolioDetailItem(
    @SerializedName("CloseTrades")
    val closeTrades: List<CloseTrade>,
    @SerializedName("FifoPortfolio")
    val fifoPortfolio: List<FifoPortfolio>
)