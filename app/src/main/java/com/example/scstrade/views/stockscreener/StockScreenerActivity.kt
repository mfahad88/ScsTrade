package com.example.scstrade.views.stockscreener

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityStockScreenerBinding
import com.example.scstrade.views.BaseActivity
import com.example.scstrade.views.stockscreener.customscreener.CustomScreenerFragment
import com.example.scstrade.views.stockscreener.fundamental.FundamentalFragment
import com.example.scstrade.views.stockscreener.technicals.TechnicalsFragment
import com.google.android.material.tabs.TabLayout

class StockScreenerActivity : BaseActivity() {
    lateinit var binding:ActivityStockScreenerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStockScreenerBinding.inflate(LayoutInflater.from(this))
        binding.toolbar.binding.market.text = getString(R.string.stock_screener)
        setContentView(binding.root)
        onBackPressedDispatcher.addCallback(this) {
            // Custom back logic
            if (supportFragmentManager.backStackEntryCount > 0) {

                supportFragmentManager.popBackStack()
                supportFragmentManager.addOnBackStackChangedListener {
                    val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                    when (currentFragment) {
                        is FundamentalFragment -> binding.tabLayout.getTabAt(1)?.select()
                        is TechnicalsFragment -> binding.tabLayout.getTabAt(2)?.select()
                        else -> binding.tabLayout.getTabAt(0)?.select()
                    }
                }

                } else {
                finish()
            }
        }
        binding.tabLayout.getTabAt(0)?.select()
        binding.sector.text = "Custom Screener"
        binding.tabLayout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(binding.tabLayout.selectedTabPosition){
                    1->{
                        binding.sector.text = "Fundamentals"
                        loadFragment("fundamentals"){
                            FundamentalFragment()
                        }
                    }
                    2 -> {
                        binding.sector.text = "Technicals"
                        loadFragment("technicals"){
                            TechnicalsFragment()
                        }
                    }
                    0->{
                        binding.sector.text = "Custom Screener"
                        loadFragment("technicals"){
                            CustomScreenerFragment()
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

    }



    fun loadFragment(tag: String, newInstance: () -> Fragment){
        val fm = supportFragmentManager
        val current = fm.findFragmentById(R.id.fragment_container)

        val tx = fm.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .setReorderingAllowed(true)

        // 1. Hide the fragment that’s currently visible
        current?.let { tx.hide(it) }

        // 2. Look for a cached instance with this tag
        val cached = fm.findFragmentByTag(tag)

        if (cached == null) {
            // First time → add, *not* replace
            tx.add(R.id.fragment_container, newInstance(), tag)
            tx.addToBackStack(tag)             // makes Back button work

        } else {
            // Second time → just show the already‑created instance
            tx.show(cached)
            fm.popBackStack(tag, 0)            // bring its BackStackEntry to top
        }

        tx.commit()
     /*   supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .addToBackStack(null)
            .commit()*/
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