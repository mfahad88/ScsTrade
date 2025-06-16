package com.example.scstrade.views.fundamental

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityFundamentalDetailBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.BaseActivity
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.fundamental.adapter.FundamentalDetailAdapter
import com.example.scstrade.views.snapshot.SnapshotActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FundamentalDetailActivity : BaseActivity() {
    lateinit var binding: ActivityFundamentalDetailBinding
    lateinit var viewModel: SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (this.application as MyApp).viewModel
        binding = ActivityFundamentalDetailBinding.inflate(LayoutInflater.from(this))
        enableEdgeToEdge()
        Utils.setEdgeToEdgeWithWhiteIcons(this)
        setContentView(binding.root)
        binding.toolbar.toggleToolbar(false)
        binding.toolbar.binding.market.text = "Fundamentals"
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel.getFundamentalDetail(intent.extras?.getString(AppConstants.TECHNICAL_SELECTION)?:"")

        viewModel.mutableFundamentalDetail.observe(this, Observer {
            when(it){
                is Resource.Error -> {
                    binding.loader.visibility = View.GONE
                    Utils.showError(binding.root,it.message?:"An error occurred")
                }
                is Resource.Loading -> binding.loader.visibility=View.VISIBLE
                is Resource.Success -> {
                    binding.loader.visibility = View.GONE
                    binding.recyclerView.apply {
                        adapter = FundamentalDetailAdapter(it.data?: emptyList(),viewModel){

                            val intent= Intent(this@FundamentalDetailActivity, SnapshotActivity::class.java)
                            intent.putExtra(AppConstants.SYMBOL, it.symbol)
                            startActivity(intent)
                        }
                        layoutManager= LinearLayoutManager(this@FundamentalDetailActivity,
                            LinearLayoutManager.VERTICAL,false)
                    }
                }
            }
        })

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