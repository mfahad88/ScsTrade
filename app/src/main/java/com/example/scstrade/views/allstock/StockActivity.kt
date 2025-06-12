package com.example.scstrade.views.allstock

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityStockBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.BaseActivity
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.landing.LandingFragment
import com.example.scstrade.views.market.MarketFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StockActivity : BaseActivity() {
    lateinit var binding: ActivityStockBinding
    private lateinit var sharedViewModel:SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        Utils.setEdgeToEdgeWithWhiteIcons(this)
        binding=ActivityStockBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)


     /*   ViewCompat.setOnApplyWindowInsetsListener(binding.fragmentContainer){v,insets->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/


        sharedViewModel=(application as MyApp).viewModel

        val b= intent.extras
        if(b!=null) {
            val fragment = AllStockFragment()
            if(b.getString("index")!=null) {
                binding.toolbar.binding.titleItem.text = b.getString("index")?.replace("Index", "")
            }else if (b.getString("sector")!=null){
                binding.toolbar.binding.titleItem.text = b.getString("sector")
            }
            fragment.arguments =b
            loadFragment(fragment)
        }

    }

    public fun loadFragment(fragment: Fragment, isBackStack:Boolean = false) {
        if(isBackStack){
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }else{
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
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
        }else if (diagonalInches>4.9 && diagonalInches<6.9){
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