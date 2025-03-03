package com.example.scstrade.model.response.news.dawn

import com.fasterxml.jackson.dataformat.xml.annotation.*

@JacksonXmlRootElement(localName = "rss")
data class RssFeed(
    @JacksonXmlProperty(localName = "channel")
    val channel: Channel
)

data class Channel(
    @JacksonXmlProperty(localName = "title")
    val title: String,

    @JacksonXmlProperty(localName = "link")
    val link: String,

    @JacksonXmlProperty(localName = "description")
    val description: String,

    @JacksonXmlProperty(localName = "language")
    val language: String,

    @JacksonXmlProperty(localName = "pubDate")
    val pubDate: String,

    @JacksonXmlProperty(localName = "lastBuildDate")
    val lastBuildDate: String,

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "item")
    val items: List<RssItem>
)

data class RssItem(
    @JacksonXmlProperty(localName = "title")
    val title: String,

    @JacksonXmlProperty(localName = "link")
    val link: String,

    @JacksonXmlProperty(localName = "description")
    val description: String,

    @JacksonXmlProperty(localName = "content:encoded")
    val contentEncoded: String? = null,

    @JacksonXmlProperty(localName = "category")
    val category: String? = null,

    @JacksonXmlProperty(localName = "guid")
    val guid: String,

    @JacksonXmlProperty(localName = "pubDate")
    val pubDate: String,

    @JacksonXmlProperty(localName = "author", isAttribute = false)
    val author: String? = null,

    @JacksonXmlProperty(localName = "media:content")
    val mediaContent: MediaContent?
)

data class MediaContent(
    @JacksonXmlProperty(localName = "url", isAttribute = true)
    val url: String,

    @JacksonXmlProperty(localName = "type", isAttribute = true)
    val type: String,

    @JacksonXmlProperty(localName = "medium", isAttribute = true)
    val medium: String? = null,

    @JacksonXmlProperty(localName = "height", isAttribute = true)
    val height: Int? = null,

    @JacksonXmlProperty(localName = "width", isAttribute = true)
    val width: Int? = null
)
