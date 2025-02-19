package com.example.scstrade.model.response.watchList


import com.google.gson.annotations.SerializedName

data class WatchListItem(
    @SerializedName("WatchListDetailID")
    val watchListDetailID: Int,
    @SerializedName("WatchListMainID")
    val watchListMainID: Int,
    @SerializedName("WatchListMainName")
    val watchListMainName: String,
    @SerializedName("WatchListPosition")
    val watchListPosition: Int,
    @SerializedName("WatchListSymbol")
    val watchListSymbol: String
)