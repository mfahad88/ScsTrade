package com.example.scstrade.model.response.portfolio


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PortfolioItemDetail(
    @SerializedName("Date")
    val date: String,
    @SerializedName("Quantity")
    val quantity: String,
    @SerializedName("Rate")
    val rate: String,
    val currentPL:Double,
    val currentPercentPL:Double
) : Parcelable