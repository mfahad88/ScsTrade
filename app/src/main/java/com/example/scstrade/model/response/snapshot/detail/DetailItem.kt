package com.example.scstrade.model.response.snapshot.detail


import com.google.gson.annotations.SerializedName

data class DetailItem(
    @SerializedName("SnapShot")
    val snapShot: SnapShot,
    @SerializedName("symbol")
    val symbol: String
)