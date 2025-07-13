package com.example.scstrade.views.globalmarket
import androidx.compose.ui.res.dimensionResource

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.R
import com.example.scstrade.databinding.ItemGlobalMarketBinding
import com.example.scstrade.databinding.ItemStocksBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.globalMarket.GlobalMarketItem
import com.example.scstrade.views.allstock.ListItem
import com.example.scstrade.views.allstock.StockAdapter.StockViewHolder
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Collections

class GlobalStockAdapter(private val onItemClick: (GlobalMarketItem) -> Unit) : ListAdapter<GlobalMarketItem,GlobalStockAdapter.GlobalStockViewHolder>(StockDiffCallback()) {
    private val previousPrices = mutableMapOf<String, Double>()
    class GlobalStockViewHolder(private val binding: ItemGlobalMarketBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GlobalMarketItem,previousPrice: Double?, onItemClick: (GlobalMarketItem) -> Unit) {
            binding.apply {
                symbol.text = item.worldMarketName
                valueTrade.text = Utils.roundTwoDecimal(item.worldMarketPrice)
                netChange.text = "${if(item.worldMarketChange<0.0) "" else "+"}${Utils.formatDouble(item.worldMarketChange)} ${if(item.worldMarketChangeP<0.0) "" else "+"}${Utils.formatDouble(item.worldMarketChangeP)}%"
                if(item.worldMarketChange<0.0){
                    binding.netChange.setTextColor(ContextCompat.getColor(binding.root.context, R.color.md_theme_error))
                }else if(item.worldMarketChange>0.0){
                    binding.netChange.setTextColor(ContextCompat.getColor(binding.root.context, R.color.md_theme_primary))
                }else{
                    binding.netChange.setTextColor(Color.parseColor("#1A73E8"))
                }
                binding.high.text = "H: ${Utils.formatDouble(item.worldMarketHigh)}"
                binding.low.text = "L: ${Utils.formatDouble(item.worldMarketLow)}"


                if(previousPrice==null){
                    binding.cardValueTrade.setCardBackgroundColor(Color.TRANSPARENT)
                    binding.cardValueTrade.alpha=1f
                }else{
                    val diff=
                        BigDecimal(item.worldMarketPrice).setScale(2, RoundingMode.HALF_UP).toDouble().minus(previousPrice)
                    if(diff>0){
                        binding.cardValueTrade.setCardBackgroundColor(ContextCompat.getColor(binding.root.context,R.color.green_increse))
                    }else if (diff<0){
                        binding.cardValueTrade.setCardBackgroundColor(ContextCompat.getColor(binding.root.context,R.color.red_decrease))
                    }else{
                        binding.cardValueTrade.setCardBackgroundColor(Color.TRANSPARENT)
                    }

                    binding.cardValueTrade.postDelayed({
                        binding.cardValueTrade.setCardBackgroundColor(Color.TRANSPARENT)
                    },3000)
                }
            }
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GlobalStockViewHolder {
        val binding = ItemGlobalMarketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GlobalStockViewHolder(binding)
    }


    override fun onBindViewHolder(holder: GlobalStockViewHolder, position: Int) {
        val current = getItem(position)
        val previousPrice = previousPrices[current.worldMarketName]
        holder.bind(getItem(position),previousPrice, onItemClick)
        previousPrices[current.worldMarketName] = current.worldMarketPrice

    }




    private class StockDiffCallback :  DiffUtil.ItemCallback<GlobalMarketItem>(){



        override fun areItemsTheSame(
            oldItem: GlobalMarketItem,
            newItem: GlobalMarketItem
        ): Boolean {
           return  oldItem.worldMarketName == newItem.worldMarketName
        }

        override fun areContentsTheSame(
            oldItem: GlobalMarketItem,
            newItem: GlobalMarketItem
        ): Boolean {
            return oldItem==newItem
        }




    }
}
