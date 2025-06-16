package com.example.scstrade.views.technicals

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
import com.example.scstrade.databinding.ActivityTechnicalsBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.BaseActivity
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.technicals.adapter.TechnicalAdapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TechnicalsActivity : BaseActivity() {
    lateinit var binding: ActivityTechnicalsBinding
    lateinit var viewModel: SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityTechnicalsBinding.inflate(LayoutInflater.from(this))
        viewModel = (this.application as MyApp).viewModel
        enableEdgeToEdge()
        Utils.setEdgeToEdgeWithWhiteIcons(this)
        setContentView(binding.root)
        binding.toolbar.toggleToolbar(false)
        binding.toolbar.binding.market.text = "Technicals"
        viewModel.getTechnicals()
        observerTechnicals()
        binding.recyclerView.apply {
            layoutManager=LinearLayoutManager(this@TechnicalsActivity,LinearLayoutManager.VERTICAL,false)
        }


    }

    private fun observerTechnicals() {
        viewModel.mutableTechnical.observe(this@TechnicalsActivity, Observer { result->

            when(result){
                is Resource.Error -> {
                    binding.loader.visibility  = View.GONE
                    Utils.showError(binding.root,result.message?:"An Error Occurred")
                }
                is Resource.Loading -> binding.loader.visibility  = View.VISIBLE
                is Resource.Success -> {
                    binding.loader.visibility  = View.GONE
                    binding.recyclerView.adapter = TechnicalAdapter(result.data?: emptyList()){
                        val intent= Intent(this@TechnicalsActivity,TechnicalsDetailActivity::class.java)
                        intent.putExtra(AppConstants.TECHNICAL_SELECTION,it.technicals)
                        startActivity(intent)
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