package com.example.scstrade.model.response.news.brecoder

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "rss")
@JsonIgnoreProperties(ignoreUnknown = true)
data class RssWrapper(
    @JacksonXmlProperty(localName = "channel")
    val channel: Channel?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Channel(
    @JacksonXmlProperty(localName = "title")
    val title: String?,

    @JacksonXmlProperty(localName = "link")
    val link: String?,

    @JacksonXmlProperty(localName = "description")
    val description: String?,

    @JacksonXmlProperty(localName = "language")
    val language: String?,

    @JacksonXmlProperty(localName = "copyright")
    val copyright: String?,

    @JacksonXmlProperty(localName = "pubDate")
    val pubDate: String?,

    @JacksonXmlProperty(localName = "lastBuildDate")
    val lastBuildDate: String?,

    @JacksonXmlProperty(localName = "ttl")
    val ttl: Int?,

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "item")
    val items: List<Item>?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Item(
    @JacksonXmlProperty(localName = "title")
    val title: String?,

    @JacksonXmlProperty(localName = "link")
    val link: String?,

    @JacksonXmlProperty(localName = "description")
    val description: String?,

    @JacksonXmlProperty(localName = "category")
    val category: String?,

    @JacksonXmlProperty(localName = "guid")
    val guid: String?,

    @JacksonXmlProperty(localName = "pubDate")
    val pubDate: String?,

    @JacksonXmlProperty(localName = "author")
    val author: String?,

    @JacksonXmlProperty(localName = "content" , namespace = "media")
    val mediaContent: MediaContent?,

)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MediaContent(
    @JacksonXmlProperty(localName = "url", isAttribute = true)
    val url: String,
    @JacksonXmlProperty(localName = "type", isAttribute = true)
    val type: String? = null,
    @JacksonXmlProperty(localName = "medium", isAttribute = true)
    val medium: String? = null,
    @JacksonXmlProperty(localName = "height", isAttribute = true)
    val height: Int? = null,
    @JacksonXmlProperty(localName = "width", isAttribute = true)
    val width: Int? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MediaThumbnail(
    @JacksonXmlProperty(localName = "url", isAttribute = true)
    val url: String
)

