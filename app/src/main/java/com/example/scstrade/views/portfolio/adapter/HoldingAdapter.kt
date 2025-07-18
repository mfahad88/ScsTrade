package com.example.scstrade.views.portfolio.adapter
import androidx.compose.ui.res.dimensionResource

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.R
import com.example.scstrade.databinding.ItemHoldingBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.portfolio.PortfolioDetailItem
import com.example.scstrade.model.response.portfolio.PortfolioItemDetail

import java.util.Collections

class HoldingAdapter(private val itemList: List<PortfolioItemDetail>, private val onItemClick: (PortfolioItemDetail) -> Unit,private val onItemEditClick: (PortfolioItemDetail) -> Unit,
                     private val onItemDeleteClick: (PortfolioItemDetail) -> Unit) : RecyclerView.Adapter<HoldingAdapter.HoldingViewHolder>() {
    class HoldingViewHolder(private val binding: ItemHoldingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: PortfolioItemDetail,
            onItemClick: (PortfolioItemDetail) -> Unit,
            onItemEditClick: (PortfolioItemDetail) -> Unit,
            onItemDeleteClick: (PortfolioItemDetail) -> Unit
        ) {
            binding.apply {
                buyDate.text = Utils.convertDateString(item.date,"dd-MMM-yyyy")
                shares.text = "${item.quantity}"
                netPrice.text = "${Utils.roundTwoDecimal(item.rate.toDouble())}"
                netCost.text = "${Utils.roundTwoDecimal(item.rate.toDouble().times(item.quantity.toDouble()))}"
                currentPl.text = "${Utils.roundTwoDecimal(item.currentPL)}"
                currentPlPercent.text = "(${Utils.roundTwoDecimal(item.currentPercentPL)}%)"
//                currentPLValue.setTextColor(if(currentPLValue.text.contains("-")) ContextCompat.getColor(itemView.context, R.color.md_theme_errorContainer) else ContextCompat.getColor(itemView.context, R.color.md_theme_primary))
              /*  threeDots.setOnClickListener {
                    if(binding.floatingMenu.visibility == View.GONE) {
                        binding.floatingMenu.visibility = View.VISIBLE
                    }else{
                        binding.floatingMenu.visibility = View.GONE
                    }
                }

                edit.setOnClickListener {
                    onItemEditClick(item)
                    binding.floatingMenu.visibility = View.GONE
                }
                delete.setOnClickListener {
                    onItemDeleteClick(item)
                    binding.floatingMenu.visibility = View.GONE
                }*/
//                currentPLValue.text = "${Utils.roundTwoDecimal((currentPrice - item.portfolioRate).times(item.portfolioQuantity))}" +
//                        "(${Utils.roundTwoDecimal((((currentPrice - item.portfolioRate).times(item.portfolioQuantity)).div(item.portfolioRate.times(item.portfolioQuantity))).times(100))}%)"
            }
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoldingViewHolder {
        val binding = ItemHoldingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HoldingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HoldingViewHolder, position: Int) {
        holder.bind(itemList[position], onItemClick,onItemEditClick,onItemDeleteClick)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setcurrentPrice(currentPrice:Double){
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
