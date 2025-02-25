package com.example.scstrade.views.fundamental

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.databinding.ItemTechnicalDetailBinding
import com.example.scstrade.model.response.fundamental.FundamentalDetailData

import java.util.Collections

class FundamentalDetailAdapter(private val itemList: List<FundamentalDetailData>, private val onItemClick: (FundamentalDetailData) -> Unit) : RecyclerView.Adapter<FundamentalDetailAdapter.FundamentalDetailViewHolder>() {

    class FundamentalDetailViewHolder(private val binding: ItemTechnicalDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FundamentalDetailData, onItemClick: (FundamentalDetailData) -> Unit) {
            binding.apply {
                symbol.text=item.symbol
                ePE.text=item.ePE.toString()
                price.text = item.price.toString()
//                avgVol.text = item.avgVol
                companyName.text = item.companyName
            }
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FundamentalDetailViewHolder {
        val binding = ItemTechnicalDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FundamentalDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FundamentalDetailViewHolder, position: Int) {
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
