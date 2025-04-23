package com.example.scstrade.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShareInHand(val symbol:String,val totalCost:String,val avgBuy:String,val marketValue:String,val share:String,val dayPL:String,val percentdayPL:String,val totalPL:String,val percenttotalPL:String):Parcelable
