package com.example.scstrade.views.fundamental.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.scstrade.R
import com.example.scstrade.databinding.ItemFundamentDetailBinding
import com.example.scstrade.databinding.ItemFundamentalBinding
import com.example.scstrade.databinding.ItemTechnicalDetailBinding
import com.example.scstrade.model.response.fundamental.FundamentalDetailData
import com.example.scstrade.viewmodels.SharedViewModel

import java.util.Collections

class FundamentalDetailAdapter(private val itemList: ArrayList<Array<String>>,private val sharedViewModel: SharedViewModel, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<FundamentalDetailAdapter.FundamentalDetailViewHolder>() {

    class FundamentalDetailViewHolder(private val binding: ItemFundamentDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Array<String>,sharedViewModel: SharedViewModel, onItemClick: (String) -> Unit) {
            binding.apply {
                symbol.text=item[0]
                ePE.text=item[1]
                price.text = item[3]
                companyName.text = item[2]

                val logo=sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(item[0]) }?.map { it.companyLogo }?.first()
                Glide.with(binding.root.context).load(logo).circleCrop().into(binding.imageViewLogo)
            }
            binding.root.setOnClickListener { onItemClick(item[0]) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FundamentalDetailViewHolder {
        val binding = ItemFundamentDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
