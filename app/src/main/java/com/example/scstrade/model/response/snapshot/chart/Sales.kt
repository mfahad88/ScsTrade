package com.example.scstrade.model.response.snapshot.chart


import com.google.gson.annotations.SerializedName

data class Sales(
    @SerializedName("Price_To_Sales_Per")
    val priceToSalesPer: List<Double>,
    @SerializedName("Sales_Per_Share_PKR")
    val salesPerSharePKR: List<Double>,
    @SerializedName("Year")
    val year: List<String>
)