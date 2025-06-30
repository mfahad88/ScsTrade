package com.example.scstrade.model.response.home

import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.model.response.toppicks.TopPickItem
import com.example.scstrade.model.summary.KSEIndices

data class HomeDataResult (
    val topPickItem: List<TopPickItem>,
    val allData: List<StockItem>,
    val futureData: List<StockItem>,
    val kseIndices: List<KSEIndices>
)