package com.example.scstrade.views.portfolio.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.databinding.ItemShareInHandBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.data.ShareInHand
import com.example.scstrade.model.response.portfolio.FifoPortfolio
import com.example.scstrade.model.response.stock.StockItem

class ShareInHandAdapter(val itemList: List<FifoPortfolio>,val list:List<StockItem>, private val onItemClick: (FifoPortfolio) -> Unit,private val onItemClickSnapshot: (FifoPortfolio) -> Unit) : RecyclerView.Adapter<ShareInHandAdapter.ShareInHandViewHolder>() {

    class ShareInHandViewHolder(private val binding: ItemShareInHandBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item:FifoPortfolio,stockItem: StockItem, onItemClick: (FifoPortfolio) -> Unit,onItemClickSnapshot: (FifoPortfolio) -> Unit) {
            Log.e("Item--->",item.toString())
            val totalCost = item.price.toDouble().times(item.quantity.toInt())
            val marketValue = stockItem.cL.times(item.quantity.toInt())
            val avgCost = item.price.toDouble()
            val dayPL = stockItem.cH.times(item.quantity.toInt())
            val percentDayPL = (dayPL.div(totalCost-dayPL)).times(100)
            val totalPL = marketValue - totalCost
            val percentTotalPL = (totalPL.div(totalCost)).times(100)
            binding.symbol.text = item.symbol
            binding.price2133.text = Utils.roundTwoDecimal(stockItem.cL)
//            Utils.animatedValueChange(binding.totalCostValue,0.00,totalCost)
            binding.totalCostValue.text = Utils.roundTwoDecimal(totalCost)
            binding.avgBuyValue.text = Utils.roundTwoDecimal(avgCost)
            binding.marketValue.text = "${Utils.roundTwoDecimal(marketValue)}"
            binding.shareValue.text = item.quantity
            binding.daysPL.text =Utils.roundTwoDecimal(dayPL)
            binding.daysPercentPL.text = "(${Utils.roundTwoDecimal(percentDayPL)}%)"
            binding.totalPL.text = Utils.roundTwoDecimal(totalPL)
            binding.totalPercentPL.text = "(${Utils.roundTwoDecimal(percentTotalPL)}%)"
            binding.btnSell.setOnClickListener {
                onItemClick(item)
            }
            binding.threeDots.setOnClickListener {
                Utils.showPopup(binding.root.context,binding.threeDots,null, listOf("Edit Name")){

                }
            }
            binding.snapshotLogo.setOnClickListener {
                onItemClickSnapshot(item)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareInHandViewHolder {
        val binding = ItemShareInHandBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShareInHandViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShareInHandViewHolder, position: Int) {


        holder.bind(itemList[position],list.filter { it.sYM.equals(itemList[position].symbol,true) }.first(), onItemClick,onItemClickSnapshot)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }


}
