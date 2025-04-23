package com.example.scstrade.views.portfolio.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.databinding.ItemHoldingBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.portfolio.PortfolioDetailItem
import com.example.scstrade.model.response.portfolio.PortfolioItem

import java.util.Collections

class HoldingAdapter(private val itemList: List<PortfolioDetailItem>, private val onItemClick: (PortfolioDetailItem) -> Unit) : RecyclerView.Adapter<HoldingAdapter.HoldingViewHolder>() {

    class HoldingViewHolder(private val binding: ItemHoldingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PortfolioDetailItem, onItemClick: (PortfolioDetailItem) -> Unit) {
            binding.apply {
                dateValue.text = Utils.convertDateString(item.portfolioDate,"dd-MM-yyyy")
                sharesValue.text = "${item.portfolioQuantity}"
                netPriceValue.text = "${item.portfolioRate}"
                netCostValue.text = "${item.portfolioRate.times(item.portfolioQuantity)}"
            }
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoldingViewHolder {
        val binding = ItemHoldingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HoldingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HoldingViewHolder, position: Int) {
        holder.bind(itemList[position], onItemClick)
    }

    override fun getItemCount(): Int {
        return itemList.size
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
