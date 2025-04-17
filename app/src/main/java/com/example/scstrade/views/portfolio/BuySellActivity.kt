package com.example.scstrade.views.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityBuySellBinding
import com.example.scstrade.helper.AppConstants

class BuySellActivity : AppCompatActivity() {
    lateinit var binding: ActivityBuySellBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBuySellBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        if(intent.getBooleanExtra(AppConstants.IS_Sell,false)){
            findViewById<View>(R.id.sell_container).visibility = View.VISIBLE
//            (binding.sellContainer as View).visibility = View.VISIBLE
        }
        if(intent.getBooleanExtra(AppConstants.IS_BUY,false)){
            findViewById<View>(R.id.buy_container).visibility = View.VISIBLE
//            (binding.buyContainer as View).visibility = View.VISIBLE
        }
        if(intent.getBooleanExtra(AppConstants.IS_Dividend,false)){
            findViewById<View>(R.id.dividend_container).visibility = View.VISIBLE
//            (binding.dividendContainer as View).visibility = View.VISIBLE
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}