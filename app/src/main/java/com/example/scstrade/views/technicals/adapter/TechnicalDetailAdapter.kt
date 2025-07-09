package com.example.scstrade.views.technicals.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.scstrade.databinding.ItemTechnicalDetailBinding
import com.example.scstrade.viewmodels.SharedViewModel
import java.util.Collections

class TechnicalDetailAdapter(private val itemList: MutableList<Array<String>>, private val sharedViewModel: SharedViewModel, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<TechnicalDetailAdapter.TechnicalDetailViewHolder>() {

    class TechnicalDetailViewHolder(private val binding: ItemTechnicalDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Array<String>,sharedViewModel: SharedViewModel, onItemClick: (String) -> Unit) {
            binding.apply {
                symbol.text=item[0]
                ePE.text=item[1]
                price.text = item[2]
                av.text = item[3]
                companyName.text = item[4]

                val logo=sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(item[0]) }?.map { it.companyLogo }?.first()
                Glide.with(binding.root.context).load(logo).circleCrop().into(binding.imageViewLogo)
            }
            binding.root.setOnClickListener { onItemClick(item[0]) }
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
