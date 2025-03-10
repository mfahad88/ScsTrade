package com.example.scstrade.model.response.snapshot.chart


import com.google.gson.annotations.SerializedName

data class BookValue(
    @SerializedName("Book_Value_PKR")
    val bookValuePKR: List<Double>,
    @SerializedName("Price_To_Book_Value_X")
    val priceToBookValueX: List<Double>,
    @SerializedName("Year")
    val year: List<String>
)