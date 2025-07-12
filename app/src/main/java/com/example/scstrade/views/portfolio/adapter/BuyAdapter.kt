package com.example.scstrade.views.portfolio.adapter
import androidx.compose.ui.res.dimensionResource

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.databinding.ItemBuyStockBinding
import com.example.scstrade.databinding.ItemHoldingBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.portfolio.PortfolioDetails
import java.util.Collections

class BuyAdapter(private val itemList: List<PortfolioDetails>, private val onItemEdit: (PortfolioDetails) -> Unit, private val onItemDelete: (PortfolioDetails) -> Unit) : RecyclerView.Adapter<BuyAdapter.BuyViewHolder>() {

    class BuyViewHolder(private val binding: ItemBuyStockBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PortfolioDetails, onItemEdit: (PortfolioDetails) -> Unit,onItemDelete: (PortfolioDetails) -> Unit) {
            binding.apply {
                dateValue.setText(Utils.convertDateString(item.portfolioDate,"dd-MMM-yyyy"))
                sharesValue.setText(item.portfolioQuantity.toString())
                netPriceValue.setText(item.portfolioRate.toString())
//                netCostValue.setText(item.portfolioQuantity.times(item.portfolioRate).toString())
                threeDots.setOnClickListener {
                    Utils.showPopup(binding.root.context,binding.threeDots,null, listOf("Edit","Delete")){
                        if(it.contains("Edit",true)){
                            onItemEdit(item)
                        }else{
                            onItemDelete(item)
                        }
                    }
                }
            }
//            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyViewHolder {
        val binding = ItemBuyStockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BuyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BuyViewHolder, position: Int) {
        holder.bind(itemList[position], onItemEdit,onItemDelete)
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
