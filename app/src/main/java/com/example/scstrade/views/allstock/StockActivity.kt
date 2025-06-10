package com.example.scstrade.views.allstock

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
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.landing.LandingFragment
import com.example.scstrade.views.market.MarketFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StockActivity : AppCompatActivity() {
    lateinit var binding: ActivityStockBinding
    private lateinit var sharedViewModel:SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        Utils.setEdgeToEdgeWithWhiteIcons(this)
        binding=ActivityStockBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(binding.fragmentContainer){v,insets->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


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

}