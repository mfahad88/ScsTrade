package com.example.scstrade.model.response.aof.city


import com.google.gson.annotations.SerializedName

data class CityDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("ProvinceCode")
    val provinceCode: String
)