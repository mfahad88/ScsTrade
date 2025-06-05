package com.example.scstrade.views.sector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.R
import com.example.scstrade.databinding.ItemSectorBinding
import java.util.Collections

class SectorAdapter(private var itemList: List<String>, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<SectorAdapter.SectorViewHolder>() {

    class SectorViewHolder(private val binding: ItemSectorBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String, onItemClick: (String) -> Unit) {
            binding.textView.text=item
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectorViewHolder {
        val binding = ItemSectorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SectorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SectorViewHolder, position: Int) {
        holder.bind(itemList[position], onItemClick)
        if(itemList.size==position){
            holder.itemView.findViewById<LinearLayout>(R.id.divider).visibility = View.GONE
        }else{
            holder.itemView.findViewById<LinearLayout>(R.id.divider).visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun addItems(itemList:List<String>){
        this.itemList=itemList
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
