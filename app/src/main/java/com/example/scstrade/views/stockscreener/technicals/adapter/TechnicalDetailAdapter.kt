package com.example.scstrade.views.stockscreener.technicals.adapter
import androidx.compose.ui.res.dimensionResource

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.scstrade.R
import com.example.scstrade.databinding.ItemTechnicalDetailBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.viewmodels.SharedViewModel
import java.util.Collections

class TechnicalDetailAdapter(private val itemList: MutableList<Array<String>>, private val sharedViewModel: SharedViewModel, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<TechnicalDetailAdapter.TechnicalDetailViewHolder>() {

   inner class TechnicalDetailViewHolder(private val binding: ItemTechnicalDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Array<String>,sharedViewModel: SharedViewModel, onItemClick: (String) -> Unit) {
            binding.apply {
                symbol.text=item[0]
                ePE.text=item[1]
                ePE.setTextColor( if (item[1].equals("buy",true)) ContextCompat.getColor(binding.root.context,
                    R.color.md_theme_primary) else ContextCompat.getColor(binding.root.context,
                    R.color.md_theme_error)
                )
                price.text = item[2]
                av.text = formatDecimal(item[3])
                companyName.text = item[4]

                val logo=sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(item[0]) }?.first()
                Utils.getCompanyLogo(binding.root.context,binding.imageViewLogo,logo)

            }
            binding.root.setOnClickListener { onItemClick(item[0]) }
        }
        private fun formatDecimal(value: String?): String {
            return value?.toDoubleOrNull()?.let {
                String.format("%,.2f", it)
            } ?: (value ?: "-")
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
