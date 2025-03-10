package com.example.scstrade.model.response.snapshot.chart


import com.google.gson.annotations.SerializedName

data class Payout(
    @SerializedName("ChartName")
    val chartName: String,
    @SerializedName("Payout_Ratio")
    val payoutRatio: List<Double>,
    @SerializedName("Year")
    val year: List<String>
)