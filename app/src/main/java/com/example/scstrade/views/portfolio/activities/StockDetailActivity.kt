package com.example.scstrade.views.portfolio.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityStockDetailBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.views.portfolio.fragments.HistoryFragment
import com.example.scstrade.views.portfolio.fragments.HoldingFragment
import com.example.scstrade.views.portfolio.fragments.SummaryFragment
import com.google.android.material.tabs.TabLayout

class StockDetailActivity : AppCompatActivity() {
    var portfolioMainID: Int=-1
    lateinit var binding:ActivityStockDetailBinding
    var symbol:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockDetailBinding.inflate(LayoutInflater.from(this))
        enableEdgeToEdge()
        Utils.setEdgeToEdgeWithWhiteIcons(this)
        setContentView(binding.root)
        portfolioMainID=intent.getIntExtra(AppConstants.PORTFOLIO_MAIN_ID,-1)
        symbol = intent.getStringExtra(AppConstants.SYMBOL).toString()
        binding.newBuyTrade.text="Buy ${symbol}"
        binding.sellTrade.text="Sell ${symbol}"
        binding.addDividend.text="Add ${symbol} Dividend"

        binding.newBuyTrade.setOnClickListener {
            val intent = Intent(this, BuySellActivity::class.java)
            intent.putExtra(AppConstants.IS_BUY,true)
            intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID,portfolioMainID)
            startActivity(intent)
            binding.floatingMenu.visibility = View.GONE
        }

        binding.sellTrade.setOnClickListener {
            val intent = Intent(this, BuySellActivity::class.java)
            intent.putExtra(AppConstants.IS_Sell, true)
            intent.putExtra(AppConstants.SYMBOL,symbol)
            intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID, portfolioMainID)
            startActivity(intent)
            binding.floatingMenu.visibility = View.GONE
        }

        binding.addDividend.setOnClickListener {
            val intent = Intent(this, BuySellActivity::class.java)
            intent.putExtra(AppConstants.IS_Dividend, true)
            intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID, portfolioMainID)
            startActivity(intent)
            binding.floatingMenu.visibility = View.GONE
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar.binding.customToolbar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.floatingActionButton.setOnClickListener {
            if(binding.floatingMenu.visibility== View.GONE){
                binding.floatingMenu.visibility=View.VISIBLE
            }else{
                binding.floatingMenu.visibility=View.GONE
            }
        }

        binding.tabLayout.getTabAt(0)?.select()
        loadFragment(SummaryFragment())
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