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

class HoldingAdapter(private val itemList: List<PortfolioItemDetail>, val stockItem: StockItem?) : RecyclerView.Adapter<HoldingAdapter.HoldingViewHolder>() {
    class HoldingViewHolder(private val binding: ItemHoldingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: PortfolioItemDetail,
            stockItem: StockItem?,
        ) {
            val currentPrice = stockItem?.cL
            val marketCost = item.quantity.toDouble().times(currentPrice?:0.0)
            val currentPL = marketCost.minus(item.rate.toDouble())
            binding.apply {
                buyDate.text = Utils.formatDateString(item.date,"M/d/yyyy hh:mm:ss a","MMM dd,yyyy")
                shares.text = "${item.quantity}"
                netPrice.text = "${Utils.roundTwoDecimal(item.rate.toDouble())}"
                netCost.text = "${Utils.roundTwoDecimal(item.quantity.toDouble().times(item.rate.toDouble()))}"
                currentPl.text = "${Utils.roundTwoDecimal(currentPL)}"
                currentPlPercent.text = "(${Utils.roundTwoDecimal(currentPL.div(item.quantity.toDouble().times(item.rate.toDouble())))}%)"

            }
//            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoldingViewHolder {
        val binding = ItemHoldingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HoldingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HoldingViewHolder, position: Int) {
        holder.bind(itemList[position],stockItem)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setcurrentPrice(currentPrice:Double){
        notifyDataSetChanged()
    }

    fun swapItems(fromPosition: Int, toPosition: Int) {
        Collections.swap(itemList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun getItemTouchHelper(): ItemTouchHelper {
        return ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                swapItems(fromPosition, toPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // No swipe action needed
            }
        })
    }
}
