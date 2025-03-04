package com.example.scstrade.model.response.news.mettis

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.annotation.*
@JsonIgnoreProperties(ignoreUnknown = true)

@JacksonXmlRootElement(localName = "rss")
data class RssFeed(
    @JacksonXmlProperty(localName = "channel")
    val channel: Channel
)
@JsonIgnoreProperties(ignoreUnknown = true)

data class Channel(
    @JacksonXmlProperty(localName = "title")
    val title: String,

    @JacksonXmlProperty(localName = "link")
    val link: String,

    @JacksonXmlProperty(localName = "description")
    val description: String,

    @JacksonXmlProperty(localName = "lastBuildDate")
    val lastBuildDate: String,

    @JacksonXmlProperty(localName = "language")
    val language: String,

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "item")
    val items: List<RssItem>,

    @JacksonXmlProperty(localName = "image")
    val image: RssImage?
)
@JsonIgnoreProperties(ignoreUnknown = true)

data class RssItem(
    @JacksonXmlProperty(localName = "title")
    val title: String,

    @JacksonXmlProperty(localName = "link")
    val link: String,

    @JacksonXmlProperty(localName = "description")
    val description: String,

    @JacksonXmlProperty(localName = "pubDate")
    val pubDate: String,

    @JacksonXmlProperty(localName = "author", isAttribute = false)
    val author: String? = null,

    @JacksonXmlProperty(localName = "category")
    val category: String? = null
)
@JsonIgnoreProperties(ignoreUnknown = true)

data class RssImage(
    @JacksonXmlProperty(localName = "url")
    val url: String,

    @JacksonXmlProperty(localName = "title")
    val title: String,

    @JacksonXmlProperty(localName = "link")
    val link: String
)
