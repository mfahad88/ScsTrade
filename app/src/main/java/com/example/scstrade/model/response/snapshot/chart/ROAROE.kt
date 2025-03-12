package com.example.scstrade.model.response.snapshot.chart


import com.google.gson.annotations.SerializedName

data class ROAROE(
    @SerializedName("ChartName")
    val chartName: String,
    @SerializedName("Return_On_Assets")
    val returnOnAssets: List<Double>,
    @SerializedName("Return_On_CE")
    val returnOnCE: List<Double>,
    @SerializedName("Return_On_Equity")
    val returnOnEquity: List<Double>,
    @SerializedName("Year")
    val year: List<String>
)