package com.example.scstrade.model.response.watchList


import com.google.gson.annotations.SerializedName

data class WatchListDetailItem(
    @SerializedName("WatchListPosition")
    val watchListPosition: Int,
    @SerializedName("WatchListSymbol")
    val watchListSymbol: String
)