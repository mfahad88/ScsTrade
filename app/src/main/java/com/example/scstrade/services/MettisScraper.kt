package com.example.scstrade.services

import com.example.scstrade.model.response.news.mettis.NewsItem
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import java.net.URL

class MettisScraper {

    fun fetchArticles(callback: (List<NewsItem>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val baseUrl = "https://mettisglobal.news/latest/"
            val articles = mutableListOf<NewsItem>()

            try {
                val doc = Jsoup.connect(baseUrl).get()
                val links = doc.select("div.post.PostList a")
                    .mapNotNull { it.attr("href") }
                    .filter { it.contains("https://mettisglobal.news/") }
                    .distinct()
                    .take(10)

                val deferredList = links.map { link ->
                    async {
                        try {
                            val articleDoc = Jsoup.connect(link).get()
                            val title = articleDoc.select("meta[property=og:title]").attr("content")
                            val desc = articleDoc.select("meta[property=og:description]").attr("content")
                            val image = articleDoc.select("meta[property=og:image]").attr("content")
                            val rawDate = articleDoc.select("div.entry-date").first()?.text()
                                ?: articleDoc.select(".entry-header time").first()?.text()

                            var date = ""
                            var time = ""
                            rawDate?.let {
                                val parts = it.split(" ")
                                if (parts.size >= 3) {
                                    date = parts.take(3).joinToString(" ")
                                }
                                if (parts.size >= 4) {
                                    time = parts.drop(3).joinToString(" ")
                                }
                            }

                            if (title.isNotBlank()) {

                                NewsItem(
                                    title = title,
                                    imageUrl = image,
                                    date = date,
                                    time = time,
                                    description = if (desc.isNotBlank()) desc else title,
                                    newsLink = link
                                )
                            } else null
                        } catch (e: Exception) {
                            null
                        }
                    }
                }

                val results = deferredList.awaitAll().filterNotNull()
                withContext(Dispatchers.Main) {
                    callback(results)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback(emptyList())
                }
            }
        }
    }
}