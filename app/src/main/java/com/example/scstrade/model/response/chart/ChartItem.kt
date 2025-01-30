package com.example.scstrade.model.response.chart


import com.google.gson.annotations.SerializedName

data class ChartItem(
    @SerializedName("trading_close")
    val tradingClose: Double,
    @SerializedName("trading_date")
    val tradingDate: String,
    @SerializedName("trading_high")
    val tradingHigh: Double,
    @SerializedName("trading_low")
    val tradingLow: Double,
    @SerializedName("trading_open")
    val tradingOpen: Double,
    @SerializedName("trading_vol")
    val tradingVol: Double
)