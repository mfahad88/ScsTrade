package com.example.scstrade.model.response.videogallery


import com.google.gson.annotations.SerializedName

data class VideoGalleryListItem(
    @SerializedName("mgvideoplaylist")
    val mgvideoplaylist: String?
)