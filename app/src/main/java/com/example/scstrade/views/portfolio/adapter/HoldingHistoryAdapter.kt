package com.example.scstrade.views.portfolio.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.R
import com.example.scstrade.databinding.Item1YearHistoryBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.data.SymbolProfit
import java.util.Collections

class HistoryHoldingAdapter(val itemList: List<SymbolProfit>, private val onItemClick: (SymbolProfit) -> Unit) : RecyclerView.Adapter<HistoryHoldingAdapter.HistoryHoldingViewHolder>() {

    class HistoryHoldingViewHolder(private val binding: Item1YearHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SymbolProfit, onItemClick: (SymbolProfit) -> Unit) {
            binding.symbol.setText(item.symbol)
            binding.youMadeA.setText(binding.root.context.getString(R.string.you_made_a_,
                Utils.roundTwoDecimal(item.profit),"${Utils.roundTwoDecimal(item.profitPercent)}%"))

            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHoldingViewHolder {
        val binding = Item1YearHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryHoldingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryHoldingViewHolder, position: Int) {
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
