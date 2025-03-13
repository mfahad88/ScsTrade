package com.example.scstrade.model.response.snapshot.chart


import com.google.gson.annotations.SerializedName

data class Insurance(
    @SerializedName("ChartName")
    val chartName: String,
    @SerializedName("II_To_PAT")
    val iIToPAT: List<Double>,
    @SerializedName("UWR_To_PAT")
    val uWRToPAT: List<Double>,
    @SerializedName("Year")
    val year: List<String>
)