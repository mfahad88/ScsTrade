package com.example.scstrade.views.videogallery.adapter

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.databinding.ItemVideoCardBinding
import com.example.scstrade.model.response.videogallery.VideoGalleryItem
import com.example.scstrade.views.videogallery.FullscreenVideoActivity
import java.util.*

class VideoGalleryAdapter(
    private val context: Context,
    private val items: List<VideoGalleryItem>
) : RecyclerView.Adapter<VideoGalleryAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(val binding: ItemVideoCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: VideoGalleryItem) {
            binding.title.text = item.mGHeading
            binding.date.text = parseDotNetDate(item.mGDate)
            binding.description.text = item.mGDiscription
            binding.playlist.text = item.mGVideoPlaylist

            // Clear old view
            binding.fullscreenContainer.removeAllViews()

            val webView = WebView(binding.root.context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    mediaPlaybackRequiresUserGesture = false
                    javaScriptCanOpenWindowsAutomatically = true
                    cacheMode = WebSettings.LOAD_DEFAULT
                }

                webChromeClient = object : WebChromeClient() {
                    override fun onShowCustomView(view: android.view.View?, callback: CustomViewCallback?) {
                        val intent = Intent(context, FullscreenVideoActivity::class.java)
                        intent.putExtra("videoUrl", getEmbedUrl(item.mGVideoLink))
                        context.startActivity(intent)
                    }
                }
            }

            // Embed YouTube via iframe
            val embedUrl = getEmbedUrl(item.mGVideoLink)
            val html = """
    <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <style>
                html, body {
                    margin: 0;
                    padding: 0;
                    background-color: black;
                    height: 100%;
                }
                iframe {
                    width: 100%;
                    height: 100%;
                    border: none;
                    border-radius: 16px;
                    overflow: hidden;
                }
            </style>
        </head>
        <body>
            <iframe
                src="$embedUrl"
                allowfullscreen
                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture">
            </iframe>
        </body>
    </html>
""".trimIndent()

            webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null)
            binding.fullscreenContainer.addView(webView)
        }

        private fun getEmbedUrl(url: String?): String {
            val id = extractYouTubeId(url)
            return "https://www.youtube.com/embed/$id?fs=1"
        }

        private fun extractYouTubeId(url: String?): String? {
            url ?: return null
            val regex = Regex("(?<=v=|be/|embed/)[^&#?\\n]+")
            return regex.find(url)?.value
        }

        private fun parseDotNetDate(dotNetDate: String?): String {
            return try {
                val timestamp = dotNetDate
                    ?.replace("/Date(", "")
                    ?.replace(")/", "")
                    ?.toLongOrNull()
                if (timestamp != null) {
                    val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                    sdf.format(Date(timestamp))
                } else "-"
            } catch (e: Exception) {
                "-"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
