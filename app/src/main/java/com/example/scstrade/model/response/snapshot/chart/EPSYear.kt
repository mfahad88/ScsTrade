package com.example.scstrade.model.response.snapshot.chart


import com.google.gson.annotations.SerializedName

data class EPSYear(
    @SerializedName("ChartName")
    val chartName: String,
    @SerializedName("Earning_Per_Share")
    val earningPerShare: List<Double>,
    @SerializedName("Year")
    val year: List<String>
)