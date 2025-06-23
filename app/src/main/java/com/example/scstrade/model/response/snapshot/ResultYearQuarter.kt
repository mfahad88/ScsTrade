package com.example.scstrade.model.response.snapshot

import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.snapshot.year.YearDetailsItem

data class ResultYearQuarter(
    val yearsDetails: Resource<List<YearDetailsItem>>,
    val quartersDetails: Resource<List<YearDetailsItem>>
)