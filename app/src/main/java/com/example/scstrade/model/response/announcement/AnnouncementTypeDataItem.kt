package com.example.scstrade.model.response.announcement


import com.google.gson.annotations.SerializedName

data class AnnouncementTypeDataItem(
    @SerializedName("Type")
    val type: String
)