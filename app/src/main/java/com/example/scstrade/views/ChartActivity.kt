package com.example.scstrade.views

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityChartBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.login.LoginDataItem
import com.google.gson.reflect.TypeToken

class ChartActivity : BaseActivity() {
    lateinit var binding: ActivityChartBinding
    lateinit var login: LoginDataItem
    lateinit var indices:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartBinding.inflate(LayoutInflater.from(this))
        enableEdgeToEdge()
        indices=intent.getStringExtra("Indices")?:"KSE 100"
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.chart.settings.javaScriptEnabled = true
        binding.chart.webViewClient = WebViewClient()
        fetchUser(this)
        if(Utils.isDarkMode(this)){
            when(indices){
                "KSE All Share Index" -> binding.chart.loadUrl("https://scstrade.com/TechnicalAnalysis/TA_RealTimeChartingMobileBlackNew.aspx?userid=E${login.registrationEmail}&symbol=${"KSE All"}")
                else -> binding.chart.loadUrl("https://scstrade.com/TechnicalAnalysis/TA_RealTimeChartingMobileBlackNew.aspx?userid=E${login.registrationEmail}&symbol=${indices}")

            }

        }else{
            when(indices){
                "KSE All Share Index" -> binding.chart.loadUrl("https://scstrade.com/TechnicalAnalysis/TA_RealTimeChartingMobileNew.aspx?userid=E${login.registrationEmail}&symbol=${"KSE All"}")
                else -> binding.chart.loadUrl("https://scstrade.com/TechnicalAnalysis/TA_RealTimeChartingMobileNew.aspx?userid=E${login.registrationEmail}&symbol=${indices}")
            }
        }

    }

    private fun fetchUser(context: Context) {
        val listType = object : TypeToken<List<LoginDataItem>>() {}
        val user= Utils.getSharedPreference(context, emptyList<LoginDataItem>(),
            AppConstants.USER,listType)
        login=user.first()
        Log.e("User: ",user.toString())
    }
    override fun getResources(): Resources {
        val res = super.getResources()
        val config = Configuration(res.configuration)
        config.fontScale = 1.0f // Set font scale to default (no scaling)
        res.updateConfiguration(config, res.displayMetrics)
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