package com.example.scstrade.model.response.snapshot.chart


import com.google.gson.annotations.SerializedName

data class Profitablity(
    @SerializedName("ChartName")
    val chartName: String,
    @SerializedName("Gross_Profit_Margin")
    val grossProfitMargin: List<Double>,
    @SerializedName("Net_Profit_Margin")
    val netProfitMargin: List<Double>,
    @SerializedName("Year")
    val year: List<String>
)