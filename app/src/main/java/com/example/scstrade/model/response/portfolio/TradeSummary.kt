package com.example.scstrade.model.response.portfolio

data class TradeSummary(
        val symbol: String,
        val status: String, // "Loss" or "Gain"
        val amount: String, // e.g., "13,587"
        val percent: String // e.g., "2.58"
    )