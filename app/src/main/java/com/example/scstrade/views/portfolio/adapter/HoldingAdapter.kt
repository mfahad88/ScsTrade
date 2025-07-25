package com.example.scstrade.views.portfolio.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.databinding.ItemHoldingBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.portfolio.CloseTrade
import com.example.scstrade.model.response.portfolio.PortfolioItemDetail
import com.example.scstrade.model.response.stock.StockItem

import java.util.Collections
import kotlin.math.roundToInt

class HoldingAdapter(private val itemList: List<PortfolioItemDetail>?, val stockItem: StockItem?) : RecyclerView.Adapter<HoldingAdapter.HoldingViewHolder>() {
    class HoldingViewHolder(private val binding: ItemHoldingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: PortfolioItemDetail?,
            stockItem: StockItem?,
        ) {
            val currentPrice = stockItem?.cL
            val marketCost = item?.quantity?.toDouble()?.times(currentPrice?:0.0)
            val currentPL = marketCost?.minus(item.rate.toDouble())
            binding.apply {
                buyDate.text = Utils.formatDateString(item?.date?:"","M/d/yyyy hh:mm:ss a","MMM dd,yyyy")
                shares.text = "${item?.quantity}"
                netPrice.text = "${Utils.roundTwoDecimal(item?.rate?.toDouble())}"
                netCost.text = "%,d".format(item?.quantity?.toDouble()?.times(item.rate.toDouble())?.roundToInt())
                currentPl.text = "%,d".format(currentPL?.roundToInt())
                currentPlPercent.text = "(${Utils.roundTwoDecimal(currentPL?.div(item.quantity.toDouble().times(item.rate.toDouble())))}%)"
            }
//            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoldingViewHolder {
        val binding = ItemHoldingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HoldingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HoldingViewHolder, position: Int) {
        holder.bind(itemList?.get(position) ?:null,stockItem)
    }

    override fun getItemCount(): Int {
        return itemList?.size?:0
    }
    
}
