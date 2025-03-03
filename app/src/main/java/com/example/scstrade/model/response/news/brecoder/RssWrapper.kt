package com.example.scstrade.model.response.news.brecoder

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.annotation.*

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "rss")
data class RssWrapper(
    @JacksonXmlProperty(isAttribute = true)
    val version: String? = null,  // Nullable to avoid errors

    @JacksonXmlProperty(localName = "channel")
    val channel: RssChannel
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class RssChannel(
    val title: String,
    val link: String,
    val description: String,

    @JacksonXmlElementWrapper(useWrapping = false)
    val item: List<Item>? = emptyList() // Default value to prevent null crashes
)

data class Item(
    val title: String,
    val link: String,
    val description: String? = null,
    val category: String? = null,
    val pubDate: String? = null,
    val author: String? = null,

    @JacksonXmlProperty(localName = "media:content")
    val mediaContent: MediaContent? = null,

    @JacksonXmlProperty(localName = "media:thumbnail")
    val mediaThumbnail: MediaThumbnail? = null

)


data class MediaContent(
    @JacksonXmlProperty(isAttribute = true)
    val url: String
)


data class MediaThumbnail(
    @JacksonXmlProperty(isAttribute = true)
    val url: String
)