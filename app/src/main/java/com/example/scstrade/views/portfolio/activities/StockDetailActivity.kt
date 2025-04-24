package com.example.scstrade.views.portfolio.activities

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityStockDetailBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.views.portfolio.fragments.HistoryFragment
import com.example.scstrade.views.portfolio.fragments.HoldingFragment
import com.example.scstrade.views.portfolio.fragments.SummaryFragment
import com.google.android.material.tabs.TabLayout

class StockDetailActivity : AppCompatActivity() {
    private var portfolioMainID: Int=-1
    lateinit var binding:ActivityStockDetailBinding
    var symbol:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockDetailBinding.inflate(LayoutInflater.from(this))
        enableEdgeToEdge()
        setContentView(binding.root)
        portfolioMainID=intent.getIntExtra(AppConstants.PORTFOLIO_MAIN_ID,-1)
        symbol = intent.getStringExtra(AppConstants.SYMBOL).toString()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab!!.text!!.equals(getString(R.string.summary))){
                    loadFragment(SummaryFragment())
                }else if(tab.text!!.equals(getString(R.string.history))){
                    loadFragment(HistoryFragment())
                }else if(tab.text!!.equals(getString(R.string.holding))){
                    loadFragment(HoldingFragment())
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
                .replace(binding.fragmentContainer.id, fragment)
                .addToBackStack(null)
                .commit()
        }else{
            supportFragmentManager
                .beginTransaction()
                .replace(binding.fragmentContainer.id,fragment)
                .commit()
        }
    }
}