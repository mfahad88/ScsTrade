package com.example.scstrade.model.response.videogallery


import com.google.gson.annotations.SerializedName

data class VideoGalleryItem(
    @SerializedName("MGDate")
    val mGDate: String?,
    @SerializedName("MGDiscription")
    val mGDiscription: String?,
    @SerializedName("MGHeading")
    val mGHeading: String?,
    @SerializedName("MGID")
    val mGID: Int?,
    @SerializedName("MGStatus")
    val mGStatus: Any?,
    @SerializedName("MGVideoLink")
    val mGVideoLink: String?,
    @SerializedName("MGVideoPlaylist")
    val mGVideoPlaylist: String?
)