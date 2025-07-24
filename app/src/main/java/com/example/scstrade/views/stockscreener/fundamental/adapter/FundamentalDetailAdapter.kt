package com.example.scstrade.views.stockscreener.fundamental.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.databinding.ItemFundamentDetailBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.viewmodels.SharedViewModel
import java.util.*

class FundamentalDetailAdapter(
    private var itemList: List<Array<String>>,
    private val sharedViewModel: SharedViewModel,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<FundamentalDetailAdapter.FundamentalDetailViewHolder>() {

    class FundamentalDetailViewHolder(private val binding: ItemFundamentDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Array<String>,
            sharedViewModel: SharedViewModel,
            onItemClick: (String) -> Unit
        ) {
            binding.apply {
                symbol.text = item.getOrNull(0) ?: "-"
                ePE.text = formatDecimal(item.getOrNull(1))
                companyName.text = item.getOrNull(2) ?: "-"
                price.text = formatDecimal(item.getOrNull(3))

                val match = sharedViewModel.mutableAllData.value?.data?.firstOrNull {
                    it.sYM.equals(item.getOrNull(0), ignoreCase = true)
                }

                av.text = match?.aV?.let { formatDecimal(it.toString()) } ?: "-"
                Utils.getCompanyLogo(itemView.context, imageViewLogo, match)
            }

            binding.root.setOnClickListener {
                item.getOrNull(0)?.let { symbol ->
                    onItemClick(symbol)
                }
            }
        }

        private fun formatDecimal(value: String?): String {
            return value?.toDoubleOrNull()?.let {
                String.format("%,.2f", it)
            } ?: (value ?: "-")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FundamentalDetailViewHolder {
        val binding = ItemFundamentDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FundamentalDetailViewHolder(binding)
    }



    override fun onBindViewHolder(holder: FundamentalDetailViewHolder, position: Int) {
        holder.bind(itemList[position], sharedViewModel, onItemClick)
    }

    override fun getItemCount(): Int = itemList.size

    // Used for sorting or refreshing list
    fun submitList(newList: List<Array<String>>) {
        itemList = newList
        notifyDataSetChanged()
    }

    fun swapItems(fromPosition: Int, toPosition: Int) {
        if (itemList is MutableList) {
            Collections.swap(itemList as MutableList, fromPosition, toPosition)
            notifyItemMoved(fromPosition, toPosition)
        }
    }

    fun getItemTouchHelper(): ItemTouchHelper {
        return ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                swapItems(fromPosition, toPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // No swipe action
            }
        })
    }
}
