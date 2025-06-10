package com.example.scstrade.views.portfolio.activities

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityBuySellBinding
import com.example.scstrade.databinding.ActivityEditBuySellBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.BaseActivity
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.portfolio.fragments.BuyFragment
import com.example.scstrade.views.portfolio.fragments.SellFragment
import com.google.android.material.tabs.TabLayout

class EditBuySellActivity : BaseActivity() {
    lateinit var binding: ActivityEditBuySellBinding
    lateinit var sharedViewModel: SharedViewModel
    var portfolioMainID=-1
    var symbol=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Utils.setEdgeToEdgeWithWhiteIcons(this)
        binding = ActivityEditBuySellBinding.inflate(LayoutInflater.from(this))
        sharedViewModel = (this.application as MyApp).viewModel
        portfolioMainID=intent.getIntExtra(AppConstants.PORTFOLIO_MAIN_ID, -1)
        symbol = intent.getStringExtra(AppConstants.SYMBOL).toString()
        sharedViewModel.getPortfolioDetails(portfolioMainID)
        setContentView(binding.root)

        binding.tabLayout.getTabAt(0)?.select()
        loadFragment(BuyFragment())
        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab?.text?.toString().equals("Buy",false)){
                    loadFragment(BuyFragment())
                }else{
                    loadFragment(SellFragment())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    public fun loadFragment(fragment: Fragment, isBackStack:Boolean = false) {
        if(isBackStack){
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }else{
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
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