package com.example.scstrade.views.technicals.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.scstrade.databinding.ItemTechnicalDetailBinding
import com.example.scstrade.model.response.technicals.TechnicalDetailData
import com.example.scstrade.viewmodels.SharedViewModel
import java.util.Collections

class TechnicalDetailAdapter(private val itemList: List<TechnicalDetailData>,private val sharedViewModel: SharedViewModel, private val onItemClick: (TechnicalDetailData) -> Unit) : RecyclerView.Adapter<TechnicalDetailAdapter.TechnicalDetailViewHolder>() {

    class TechnicalDetailViewHolder(private val binding: ItemTechnicalDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TechnicalDetailData,sharedViewModel: SharedViewModel, onItemClick: (TechnicalDetailData) -> Unit) {
            binding.apply {
                symbol.text=item.symbol
                ePE.text=item.signal
                price.text = item.initiated
                avgVol.text = item.initiatedAt
                companyName.text = item.companyName

                val logo=sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(item.symbol) }?.map { it.companyLogo }?.first()
                Glide.with(binding.root.context).load(logo).circleCrop().into(binding.imageViewLogo)
            }
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TechnicalDetailViewHolder {
        val binding = ItemTechnicalDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TechnicalDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TechnicalDetailViewHolder, position: Int) {
        holder.bind(itemList[position],sharedViewModel, onItemClick)
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
