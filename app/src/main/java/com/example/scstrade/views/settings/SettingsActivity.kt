package com.example.scstrade.views.settings

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

class SettingsActivity : AppCompatActivity() {
    lateinit var binding:ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(LayoutInflater.from(this))
        enableEdgeToEdge()
        Utils.setEdgeToEdgeWithWhiteIcons(this)
        setContentView(binding.root)


        if(!Utils.getSharedPreference(this,AppConstants.LIGHT_MODE)){
            binding.switch1.isChecked = false
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            binding.switch1.isChecked = true
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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
}