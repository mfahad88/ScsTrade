package com.example.scstrade.views.watchlist

import androidx.recyclerview.widget.RecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.scstrade.databinding.ItemStocksBinding
import java.util.Collections

class WatchListDetailAdapter(private val itemList: MutableList<String>, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<WatchListDetailAdapter.WatchListDetailViewHolder>() {

    class WatchListDetailViewHolder(private val binding: ItemStocksBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String, onItemClick: (String) -> Unit) {
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchListDetailViewHolder {
        val binding = ItemStocksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WatchListDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WatchListDetailViewHolder, position: Int) {
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
