package com.example.scstrade.views.fundamental.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.scstrade.R
import com.example.scstrade.databinding.ItemTechnicalDetailBinding
import com.example.scstrade.model.response.fundamental.FundamentalDetailData
import com.example.scstrade.viewmodels.SharedViewModel

import java.util.Collections

class FundamentalDetailAdapter(private val itemList: List<FundamentalDetailData>,private val sharedViewModel: SharedViewModel, private val onItemClick: (FundamentalDetailData) -> Unit) : RecyclerView.Adapter<FundamentalDetailAdapter.FundamentalDetailViewHolder>() {

    class FundamentalDetailViewHolder(private val binding: ItemTechnicalDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FundamentalDetailData,sharedViewModel: SharedViewModel, onItemClick: (FundamentalDetailData) -> Unit) {
            binding.apply {
                symbol.text=item.symbol
                ePE.text=item.ePE.toString()
                price.text = item.price.toString()
//                avgVol.text = item.avgVol
                companyName.text = item.companyName
                val logo=sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(item.symbol) }?.map { it.companyLogo }?.first()
                Glide.with(binding.root.context).load(logo).placeholder(ContextCompat.getDrawable(binding.root.context, R.drawable.building)).circleCrop().into(binding.imageViewLogo)
            }
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FundamentalDetailViewHolder {
        val binding = ItemTechnicalDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FundamentalDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FundamentalDetailViewHolder, position: Int) {
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
