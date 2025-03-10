package com.example.scstrade.model.response.snapshot.chart


import com.google.gson.annotations.SerializedName

data class Cash(
    @SerializedName("Cash_Flow_Per_Share")
    val cashFlowPerShare: List<Double>,
    @SerializedName("Cash_Per_Share")
    val cashPerShare: List<Double>,
    @SerializedName("ChartName")
    val chartName: String,
    @SerializedName("Year")
    val year: List<String>
)