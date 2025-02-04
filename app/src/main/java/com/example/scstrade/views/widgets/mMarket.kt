package com.example.scstrade.views.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.scstrade.databinding.MmarketBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class mMarket(context: Context?, attrs: AttributeSet?=null, defStyleAttr: Int=0):
    LinearLayout(context, attrs, defStyleAttr) {
    val binding= MmarketBinding.inflate(LayoutInflater.from(context),null,true)
    var marketStatus="closed"
    lateinit var date:Date

    private fun init() {
        if(marketStatus.equals("open",true)){
            binding.open.visibility = View.VISIBLE
            binding.close.visibility = View.GONE
        }else{
            binding.open.visibility = View.GONE
            binding.close.visibility = View.VISIBLE
        }
        var sdf = SimpleDateFormat("dd MMM yyyy | hh:mma", Locale.ENGLISH);

        // Get the current date and time
        var formattedDate = sdf.format(date)
    }

    public fun setValue(marketStatus:String ,date:Date){
        this.marketStatus=marketStatus
        this.date = date
        init()
    }
}