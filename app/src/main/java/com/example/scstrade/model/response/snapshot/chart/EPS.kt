package com.example.scstrade.model.response.snapshot.chart


import com.google.gson.annotations.SerializedName

data class EPS(
    @SerializedName("ChartName")
    val chartName: String,
    @SerializedName("Q1")
    val q1: List<Double>,
    @SerializedName("Q2")
    val q2: List<Double>,
    @SerializedName("Q3")
    val q3: List<Double>,
    @SerializedName("Q4")
    val q4: List<Double>,
    @SerializedName("Year")
    val year: List<String>
)