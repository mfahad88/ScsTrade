//package com.example.scstrade.model.response.news.tribune

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.annotation.*
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement  // ✅ Correct

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "rss")
data class RssFeed(
    @field:JacksonXmlProperty(localName = "channel") val channel: Channel? = null
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class Channel(
    @field:JacksonXmlProperty(localName = "title") val title: String? = null,
    @field:JacksonXmlProperty(localName = "link") val link: String? = null,
    @field:JacksonXmlProperty(localName = "description") val description: String? = null,
    @field:JacksonXmlProperty(localName = "lastBuildDate") val lastBuildDate: String? = null,
//    @field:JacksonXmlProperty(localName = "language") val language: String? = null,
    @field:JacksonXmlProperty(localName = "generator") val generator: String? = null,
    @JacksonXmlElementWrapper(useWrapping = false)
    @field:JacksonXmlProperty(localName = "item") val items: List<RssItem>? = null
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class RssItem(
    @field:JacksonXmlProperty(localName = "title") val title: String? = null,
    @field:JacksonXmlProperty(localName = "link") val link: String? = null,
    @field:JacksonXmlProperty(localName = "comments") val comments: String? = null,
    @field:JacksonXmlProperty(localName = "pubDate") val pubDate: String? = null,
    @field:JacksonXmlProperty(localName = "creator",  namespace = "dc") val creator: String? = null, // Handling localNamespaced XML
    @field:JacksonXmlProperty(localName = "category") val category: String? = null,
    @field:JacksonXmlProperty(localName = "guid") val guid: String? = null,
    @field:JacksonXmlProperty(localName = "description") val description: String? = null,
    @field:JacksonXmlProperty(localName = "image") val image: Image? = null
)

data class Image(
    @field:JacksonXmlProperty(localName = "img") val img: Img? = null
)
data class Img(
    @field:JacksonXmlProperty(localName = "src", isAttribute = true) val src: String? = null,
    @JsonIgnoreProperties(ignoreUnknown = true)
    @field:JacksonXmlProperty(localName = "class", isAttribute = true) val className:String? = null
)
