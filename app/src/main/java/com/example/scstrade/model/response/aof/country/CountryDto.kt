package com.example.scstrade.model.response.aof.country


import com.google.gson.annotations.SerializedName

data class CountryDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)