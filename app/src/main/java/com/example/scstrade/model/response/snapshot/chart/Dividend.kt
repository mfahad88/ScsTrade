package com.example.scstrade.model.response.snapshot.chart


import com.google.gson.annotations.SerializedName

data class Dividend(
    @SerializedName("Dividend")
    val dividend: List<Double>,
    @SerializedName("Dividend_Yield_Per")
    val dividendYieldPer: List<Double>,
    @SerializedName("Year")
    val year: List<String>
)