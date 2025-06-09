package com.example.scstrade.views.portfolio.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.R
import com.example.scstrade.databinding.Item1YearHistoryBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.data.SymbolProfit
import java.util.Collections

class HistoryHoldingAdapter(/*val itemList: List<SymbolProfit>*/private val onItemClick: (SymbolProfit) -> Unit) : ListAdapter<SymbolProfit,HistoryHoldingAdapter.HistoryHoldingViewHolder>(HistoryDiffUtilsCallback()) {

    class HistoryHoldingViewHolder(private val binding: Item1YearHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SymbolProfit, onItemClick: (SymbolProfit) -> Unit) {
            binding.symbol.setText(item.symbol)
            binding.youMadeA.setText(binding.root.context.getString(R.string.you_made_a_,
                Utils.roundTwoDecimal(item.profit),"${Utils.roundTwoDecimal(item.profitPercent)}%"))

            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    class HistoryDiffUtilsCallback:DiffUtil.ItemCallback<SymbolProfit>() {
        override fun areItemsTheSame(oldItem: SymbolProfit, newItem: SymbolProfit): Boolean {
            return  oldItem.symbol.equals(newItem.symbol,true)
        }

        override fun areContentsTheSame(oldItem: SymbolProfit, newItem: SymbolProfit): Boolean {
            return oldItem==newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHoldingViewHolder {
        val binding = Item1YearHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryHoldingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryHoldingViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }

}
