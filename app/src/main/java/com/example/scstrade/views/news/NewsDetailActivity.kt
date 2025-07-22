package com.example.scstrade.views.news
import androidx.compose.ui.res.dimensionResource

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.Target
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityNewsDetailBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.BaseActivity
import com.example.scstrade.views.MyApp

class  NewsDetailActivity : BaseActivity() {
    lateinit var binding: ActivityNewsDetailBinding
    lateinit var newsType:String
    lateinit var title:String
    lateinit var sharedViewModel: SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityNewsDetailBinding.inflate(LayoutInflater.from(this))
        sharedViewModel=(this.application as MyApp).viewModel
        enableEdgeToEdge()
        Utils.setEdgeToEdgeWithWhiteIcons(this)
        setContentView(binding.root)




        newsType= intent.extras?.getString(AppConstants.NEWS_TYPE).toString()
        title = intent.extras?.getString(AppConstants.TITLE).toString()
        /*if(newsType.equals(AppConstants.SCS,true)){
            sharedViewModel.mutableNews.observe(this, Observer { result->
                when(result){
                    is Resource.Error -> Utils.showError(binding.root,result.message?:"An error occurred")
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        val data = result.data?.filter { it.newsDesc.equals(title,true) }?.first()
                        binding.cardTitle.text = data?.newsDesc
                        binding.pubDate.text = Utils.convertDate(data?.newsDate?:"")
                        binding.description.text = data?.newsDesc?.replace(Regex("Source:\\s*https?://\\S+"), "")
                        binding.cardTitle.setTextColor(Color.WHITE)
                        binding.pubDate.setTextColor(Color.WHITE)
                        binding.imageView14.setColorFilter(Color.WHITE)
                        if(!TextUtils.isEmpty(extractSource(data?.newsDesc?:""))) {
                            binding.source.visibility=View.VISIBLE
                            binding.source.setOnClickListener {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(extractSource(data?.newsDesc ?: ""))
                                )
                                this.startActivity(intent)
                            }
                        }else{
                            binding.source.visibility=View.GONE
                        }
                    }
                }
            })
        }*/
       if(newsType.equals(AppConstants.BRECODER,true)){
            sharedViewModel.mutableBrecoder.observe(this, Observer {result->
                when(result){
                    is Resource.Error -> Utils.showError(binding.root,result.message?:"An error occurred")
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        val data = result.data?.channel?.items?.filter { it.title.equals(title) }?.first()
                        if(data?.mediaContent!=null) {
                            Glide.with(this).load(data.mediaContent.url)
                                .transform(RoundedCorners(50))
                                .into(binding.imageViewNews)
                        }
                        binding.cardTitle.setTextColor(Color.WHITE)
                        binding.pubDate.setTextColor(Color.WHITE)
                        binding.imageView14.setColorFilter(Color.WHITE)
                        binding.cardTitle.text = data?.title
                        binding.pubDate.text = Utils.convertDateBrFormat(data?.pubDate?:"")
                        binding.description.text = HtmlCompat.fromHtml(data?.description?:"",
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                        binding.description.movementMethod = LinkMovementMethod.getInstance()
                        binding.source.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data?.link))
                            this.startActivity(intent)
                        }
                    }
                }
            })
        }else if(newsType.equals(AppConstants.TRIBUNE,true)){

            sharedViewModel.mutableTribune.observe(this, Observer {result->
                when(result){
                    is Resource.Error -> Utils.showError(binding.root,result.message?:"An error occurred")
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        val data = result.data?.channel?.items?.filter { it.title.equals(title) }?.first()

                        if(data?.image?.img?.src!=null) {
                            Glide.with(this).load(data.image.img.src)
                                .transform(RoundedCorners(50))
                                .into(binding.imageViewNews)
                        }
                        binding.cardTitle.setTextColor(Color.WHITE)
                        binding.pubDate.setTextColor(Color.WHITE)
                        binding.imageView14.setColorFilter(Color.WHITE)
                        binding.cardTitle.text = data?.title
                        binding.pubDate.text = Utils.convertDateBrFormat(data?.pubDate?:"")
                        binding.description.text = HtmlCompat.fromHtml(data?.content?.trim()?:"",
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                        binding.description.movementMethod = LinkMovementMethod.getInstance()
                        binding.source.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data?.link))
                            this.startActivity(intent)
                        }
                    }
                }
            })

        }else if(newsType.equals(AppConstants.PROFIT,true)){

            sharedViewModel.mutableProfit.observe(this, Observer {result->
                when(result){
                    is Resource.Error -> Utils.showError(binding.root,result.message?:"An error occurred")
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        val data = result.data?.channel?.items?.filter { it.title.equals(title) }?.first()
                        if(data?.description!=null) {
                            if (extractImage(data.description) != null) {
                                Glide.with(this).load(extractImage(data.description))
                                    .transform(RoundedCorners(50))
                                    .into(binding.imageViewNews)
                            }
                        }
                        binding.cardTitle.setTextColor(Color.WHITE)
                        binding.pubDate.setTextColor(Color.WHITE)
                        binding.imageView14.setColorFilter(Color.WHITE)
                        binding.cardTitle.text = data?.title
                        binding.pubDate.text = Utils.convertDateBrFormat(data?.pubDate?:"")
                        binding.description.text = HtmlCompat.fromHtml(data?.description?.trim()?:"",
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                        binding.description.movementMethod = LinkMovementMethod.getInstance()
                        binding.source.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data?.link))
                            this.startActivity(intent)
                        }
                    }
                }
            })

        }else if(newsType.equals(AppConstants.METTIS,true)){

            sharedViewModel.mutableMettis.observe(this, Observer {result->
                when(result){
                    is Resource.Error -> Utils.showError(binding.root,result.message?:"An error occurred")
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        val data= result.data?.filter { it.title.equals(intent.getStringExtra(AppConstants.TITLE)) }?.first()
//                        val data = result.data?.channel?.items?.filter { it.title.equals(title) }?.first()
                        Glide.with(this).load(data?.imageUrl)
                            .transform(RoundedCorners(50))
                            .into(binding.imageViewNews)
                        binding.cardTitle.setTextColor(Color.WHITE)
                        binding.pubDate.setTextColor(Color.WHITE)
                        binding.imageView14.setColorFilter(Color.WHITE)
                        binding.cardTitle.text = data?.title
//                        binding.pubDate.text = Utils.convertDateBrFormat(data?.pubDate?:"")
                        binding.description.text = HtmlCompat.fromHtml(data?.description?.trim()?:"",
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                        binding.description.movementMethod = LinkMovementMethod.getInstance()
                        binding.source.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data?.newsLink))
                            this.startActivity(intent)
                        }
                    }
                }
            })


        }else if(newsType.equals(AppConstants.DAWN,true)){
            sharedViewModel.mutableDawn.observe(this, Observer {result->
                when(result){
                    is Resource.Error -> Utils.showError(binding.root,result.message?:"An error occurred")
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        val data = result.data?.channel?.items?.filter { it.title.equals(title) }?.first()
                        if(data?.mediaContent!=null) {
                            Glide.with(this).load(data.mediaContent.url)
                                .transform(RoundedCorners(50))
                                .into(binding.imageViewNews)
                        }
                        binding.cardTitle.setTextColor(Color.WHITE)
                        binding.pubDate.setTextColor(Color.WHITE)
                        binding.imageView14.setColorFilter(Color.WHITE)
                        binding.cardTitle.text = data?.title
                        binding.pubDate.text = Utils.convertDateBrFormat(data?.pubDate?:"")
                        binding.description.text = HtmlCompat.fromHtml(data?.description?:"",
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                        binding.description.movementMethod = LinkMovementMethod.getInstance()
                        binding.source.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data?.link))
                            this.startActivity(intent)
                        }
                    }
                }
            })
        }
    }

    fun extractImage(input: String): String? {
        val regex = """src=["'](https?://[^"']+)["']""".toRegex()
        return regex.find(input)?.groupValues?.get(1) // Returns first matched group
    }

    fun extractSource(input:String): String? {
        val regex = Regex("(?<=Source:)\\s*(https?://\\S+)")
        return regex.find(input)?.value
    }

    override fun getResources(): Resources {

        val res = super.getResources()
        val config = Configuration(res.configuration)

        val metrics = res.displayMetrics

        // Calculate screen width and height in inches
        val widthInches = metrics.widthPixels / metrics.xdpi
        val heightInches = metrics.heightPixels / metrics.ydpi
        val diagonalInches = Math.sqrt((widthInches * widthInches + heightInches * heightInches).toDouble())

        // Set fontScale based on diagonal screen size
        if(diagonalInches>3.9 && diagonalInches<4.9){
            config.fontScale = 0.85f  // Small phones
        }else if (diagonalInches>4.9 && diagonalInches<5.4){
            config.fontScale = 0.95f
        }else if (diagonalInches>5.5 && diagonalInches<6.9){
            config.fontScale = 1.0f
        }else{
            config.fontScale = 1.2f
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            config.fontWeightAdjustment = 0

        }
        res.updateConfiguration(config, metrics)
        return res
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            // Override any incoming configuration changes
            overrideConfiguration.densityDpi = resources.displayMetrics.densityDpi
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }
}