package com.example.scstrade.model.response.news.profit

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

    @JacksonXmlProperty(localName = "lastBuildDate")
    val lastBuildDate: String,

    @JacksonXmlProperty(localName = "language")
    val language: String,

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

    @JacksonXmlProperty(localName = "pubDate")
    val pubDate: String,

    @JacksonXmlProperty(localName = "author", isAttribute = false)
    val author: String? = null
)
