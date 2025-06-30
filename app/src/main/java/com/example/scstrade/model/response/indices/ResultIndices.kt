package com.example.scstrade.model.response.indices

import com.example.scstrade.model.response.chart.ChartItem
import com.example.scstrade.model.summary.KSEIndices

data class ResultIndices(
    val kseIndices: List<KSEIndices>?,
    val chartItemKSEALL : List<ChartItem>?,
    val chartItemKSE100 : List<ChartItem>?,
    val chartItemKSE30 : List<ChartItem>?,
    val chartItemKMI30 : List<ChartItem>?,
)
