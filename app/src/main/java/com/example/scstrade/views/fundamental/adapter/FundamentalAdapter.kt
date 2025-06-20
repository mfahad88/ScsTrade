package com.example.scstrade.views.fundamental.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.databinding.ItemFundamentalBinding
import com.example.scstrade.databinding.ItemSectorBinding
import com.example.scstrade.model.response.fundamental.FundamentalData
import java.util.Collections

class FundamentalAdapter(private val itemList: List<FundamentalData>, private val onItemClick: (FundamentalData) -> Unit) : RecyclerView.Adapter<FundamentalAdapter.FundamentalViewHolder>() {

    class FundamentalViewHolder(private val binding: ItemFundamentalBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FundamentalData, onItemClick: (FundamentalData) -> Unit) {
            binding.textView.text = item.fundamentals
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FundamentalViewHolder {
        val binding = ItemFundamentalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FundamentalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FundamentalViewHolder, position: Int) {
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
