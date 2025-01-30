package com.example.scstrade.model.summary

import com.example.scstrade.model.response.chart.ChartItem
import com.google.gson.annotations.SerializedName

data class KSEIndices(
    @SerializedName("CURRENT_INDEX")
    val cURRENTINDEX: String,
    @SerializedName("HIGH_INDEX")
    val hIGHINDEX: String,
    @SerializedName("INDEX_CODE")
    val iNDEXCODE: String,
    @SerializedName("LOW_INDEX")
    val lOWINDEX: String,
    @SerializedName("MarketStatus")
    val marketStatus: String,
    @SerializedName("NET_CHANGE")
    val nETCHANGE: String,
    @SerializedName("PreClose")
    val preClose: Double,
    @SerializedName("VALUE_TRADED")
    val vALUETRADED: String,
    @SerializedName("VOLUME_TRADED")
    val vOLUMETRADED: String,

)