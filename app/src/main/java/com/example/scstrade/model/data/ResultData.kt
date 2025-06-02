package com.example.scstrade.model.data

import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.portfolio.DividendItem
import com.example.scstrade.model.response.portfolio.PortfolioDetailItem

data class ResultData(
    val portfolioDetails: Resource<PortfolioDetailItem>,
    val dividendItem: Resource<List<DividendItem>>
)
