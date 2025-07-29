package com.example.scstrade.views.settings
import androidx.compose.ui.res.dimensionResource

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivitySettingsBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.views.BaseActivity

class SettingsActivity : BaseActivity() {
    lateinit var binding:ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(LayoutInflater.from(this))
        enableEdgeToEdge()
        Utils.setEdgeToEdgeWithWhiteIcons(this)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Apply top and bottom padding
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            // Return insets so child views can also use them
            insets
        }

        if(!Utils.getSharedPreference(this,AppConstants.LIGHT_MODE)){
            binding.switch1.isChecked = false
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            binding.switch1.isChecked = true
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        binding.apply {
            btnSave.setOnClickListener {

                Utils.saveSharedPreference(this@SettingsActivity,AppConstants.REPEAT_DAILY,repeatDaily.isChecked)
                Utils.saveSharedPreference(this@SettingsActivity,AppConstants.DURATION,duration.value.toInt())
                Utils.saveSharedPreference(this@SettingsActivity,AppConstants.MARKET_UPDATE,marketUpdate.isChecked)

                if(duration.value>0f && marketUpdate.isChecked){
                    Utils.scheduleMarketNotification(binding.root.context,duration.value.toInt(),repeatDaily.isChecked)
                }
                Utils.showSuccess(binding.root,"Setting Saved...")
//                finish()
            }
        }

        binding.switch1.setOnCheckedChangeListener { compoundButton, b ->
            if(!b){

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                Utils.saveSharedPreference(this,AppConstants.LIGHT_MODE,false)
                finish()
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                Utils.saveSharedPreference(this,AppConstants.LIGHT_MODE,true)
                finish()
            }
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