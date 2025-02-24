package com.example.scstrade.model.response.watchList


import com.google.gson.annotations.SerializedName

data class WatchListItem(
    @SerializedName("WatchListMainID")
    val WatchListMainID: Int,
    @SerializedName("WatchListMainName")
    val WatchListMainName: String,
    @SerializedName("WatchListMainPosition")
    val WatchListMainPosition: Int,
    @SerializedName("SymbolCount")
    val SymbolCount: Int?,
    @SerializedName("WatchListMainDefault")
    val WatchListMainDefault: Boolean?
)