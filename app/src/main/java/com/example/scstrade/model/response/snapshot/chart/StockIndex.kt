package com.example.scstrade.model.response.snapshot.chart


import com.google.gson.annotations.SerializedName

data class StockIndex(
    @SerializedName("ChartName")
    val chartName: String,
    @SerializedName("Date")
    val date: List<String?>,
    @SerializedName("Index")
    val index: List<Double>,
    @SerializedName("Stock")
    val stock: List<Double>
)