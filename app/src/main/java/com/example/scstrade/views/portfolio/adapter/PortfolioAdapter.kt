package com.example.scstrade.views.portfolio.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.databinding.ItemPortfolioBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.portfolio.PortfolioItem
import java.util.Collections

class PortFolioAdapter(private val itemList: List<PortfolioItem>, private val onItemClick: (PortfolioItem) -> Unit,private val onItemPopupClick: (String, PortfolioItem) -> Unit) : RecyclerView.Adapter<PortFolioAdapter.PortFolioViewHolder>() {

    class PortFolioViewHolder(private val binding: ItemPortfolioBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PortfolioItem, onItemClick: (PortfolioItem) -> Unit,  onItemPopupClick: (String,PortfolioItem) -> Unit) {
            binding.apply {
                defaultWat.text = item.portfolioMainName
                imageViewThree.setOnClickListener {
                    Utils.showPopup(binding.root.context,binding.imageViewThree, null, listOf("Edit Name","Delete Porfolio")){
                        onItemPopupClick(it,item)
                    }
                }
            }
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortFolioViewHolder {
        val binding = ItemPortfolioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PortFolioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PortFolioViewHolder, position: Int) {
        holder.bind(itemList[position], onItemClick,onItemPopupClick)
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
