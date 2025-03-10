package com.example.scstrade.model.response.snapshot.detail

import com.google.gson.annotations.SerializedName

data class DescNameValue(
    @SerializedName("desc")
    val desc: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("value")
    val value: String
)
