package com.example.scstrade.model.response.snapshot.chart


import com.google.gson.annotations.SerializedName

data class Charting(
    @SerializedName("ADR")
    val aDR: ADR,
    @SerializedName("BookValue")
    val bookValue: BookValue,
    @SerializedName("Cash")
    val cash: Cash,
    @SerializedName("Dividend")
    val dividend: Dividend,
    @SerializedName("EPS")
    val ePS: EPS,
    @SerializedName("EPSYear")
    val ePSYear: EPSYear,
    @SerializedName("Enterprise")
    val enterprise: Any?,
    @SerializedName("Insurance")
    val insurance: Any?,
    @SerializedName("Payout")
    val payout: Payout,
    @SerializedName("Profitablity")
    val profitablity: Any?,
    @SerializedName("ROAROE")
    val rOAROE: ROAROE,
    @SerializedName("Sales")
    val sales: Any?,
    @SerializedName("StockIndex")
    val stockIndex: StockIndex
)