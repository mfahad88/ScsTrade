package com.example.scstrade.model.response.toppicks


import com.google.gson.annotations.SerializedName

data class TopPickItem(
    @SerializedName("SCSImpItemSymbol")
    val sCSImpItemSymbol: String
)