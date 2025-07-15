package com.example.scstrade.model

data class FilterValue(
    val operator: String = "",
    val min: Double? = null,
    val max: Double? = null
)