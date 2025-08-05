package com.example.scstrade.views.watchlist.adapter

import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.R
import com.example.scstrade.databinding.ItemSymbolBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.stock.StockItem
import java.util.Collections

class SymbolAdapter(
    private val itemList: List<StockItem>,
    private val onItemClick: (StockItem) -> Unit
) : RecyclerView.Adapter<SymbolAdapter.SymbolViewHolder>() {

    private var filterList = ArrayList<StockItem>()
    private val selectedItems = mutableSetOf<String>()

    inner class SymbolViewHolder(private val binding: ItemSymbolBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StockItem, onItemClick: (StockItem) -> Unit) {
            binding.symbol.text = item.sYM
            binding.companyName.text = item.nM
            Utils.getCompanyLogo(binding.root.context, binding.imageView6, item)

            val isSelected = selectedItems.contains(item.sYM)
            val iconView = binding.imageViewSelected
            val context = iconView.context

            // Set initial icon
            iconView.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    if (isSelected) R.drawable.baseline_check_circle_24 else R.drawable.baseline_add_circle_outline_24
                )
            )

            binding.main.setOnClickListener {
                // Haptic feedback
                binding.main.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)

                val wasSelected = selectedItems.contains(item.sYM)

                // Fade animations
                val fadeOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out).apply {
                    duration = 150
                }
                val fadeIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in).apply {
                    duration = 150
                }

                iconView.startAnimation(fadeOut)

                iconView.postDelayed({
                    if (wasSelected) {
                        selectedItems.remove(item.sYM)
                        Utils.showNeutral(binding.root, "${item.sYM} removed from watchlist")
                    } else {
                        selectedItems.add(item.sYM)
                        Utils.showNeutral(binding.root, "${item.sYM} added to watchlist")
                    }

                    val iconRes = if (wasSelected)
                        R.drawable.baseline_add_circle_outline_24
                    else
                        R.drawable.baseline_check_circle_24

                    iconView.setImageDrawable(ContextCompat.getDrawable(context, iconRes))
                    iconView.startAnimation(fadeIn)

                    onItemClick(item)
                    filterList(null, null) // re-sort
                }, fadeOut.duration)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymbolViewHolder {
        val binding = ItemSymbolBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SymbolViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SymbolViewHolder, position: Int) {
        holder.bind(filterList[position], onItemClick)
    }

    override fun getItemCount(): Int = filterList.size

    fun filterList(sector: String?, symbol: String?) {
        filterList.clear()

        val filtered = when {
            sector.equals("All Sector", true) && symbol.isNullOrEmpty() -> itemList
            sector.equals("All Sector", true) -> itemList.filter { it.sYM.contains(symbol ?: "", true) }
            symbol.isNullOrEmpty() -> itemList.filter { it.sN.contains(sector ?: "", true) }
            else -> itemList.filter {
                it.sN.contains(sector ?: "", true) && it.sYM.contains(symbol , true)
            }
        }

        val sorted = filtered.sortedWith(compareByDescending { selectedItems.contains(it.sYM) })
        filterList.addAll(sorted)
        notifyDataSetChanged()
    }

    fun swapItems(fromPosition: Int, toPosition: Int) {
        Collections.swap(itemList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
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
