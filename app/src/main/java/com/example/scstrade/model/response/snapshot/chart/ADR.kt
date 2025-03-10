package com.example.scstrade.model.response.snapshot.chart


import com.google.gson.annotations.SerializedName

data class ADR(
    @SerializedName("ADR")
    val aDR: List<Double>,
    @SerializedName("Cash_To_DPR")
    val cashToDPR: List<Double>,
    @SerializedName("ChartName")
    val chartName: String,
    @SerializedName("Equity_To_Ad")
    val equityToAd: List<Double>,
    @SerializedName("Year")
    val year: List<String>
)